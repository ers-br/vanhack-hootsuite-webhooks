package hoot;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import utils.A;
import utils.D;
import utils.E;
import utils.G;
import utils.IO;
import utils.M;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;

public class HootSender {

	private static Timer senderThread;
	private static Timer unlockerThread;

	private static TimerTask sendTask = new TimerTask() {
		public void run() {
			processPosting();
		}
	};

	private static TimerTask unlock = new TimerTask() {
		public void run() {
			processUnlocking();
		}
	};

	public static void init() {
		senderThread = new Timer("HootSender");
		unlockerThread = new Timer("HootUnlocker");
		senderThread.schedule(sendTask, 1000, 2000);
		unlockerThread.schedule(unlock, 10000, 10000);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Some items in the messages table were locked due to failure in sending to destination one or more times. This process lists all
	 * messages in this state that could be freed and processes them.
	 */
	static void processUnlocking() {
		ScanSpec scan = new ScanSpec();
		scan.withFilterExpression("#lockedUntil <= :now");
		scan.withNameMap(M.smap("#lockedUntil", "lockedUntil"));
		scan.withValueMap(M.map(":now", System.currentTimeMillis()));
		List<Map<String, Object>> msgs = HootDB.MSGs.scanEx(scan);
		if (msgs == null)
			return;
		for (Map<String, Object> msg : msgs) {
			String id = (String) msg.get("id");
			Object seq = msg.get("seq");
			HootDB.MSGs.update(id, seq, new AttributeUpdate("lockedUntil").delete(), new AttributeUpdate("state").put("new"));
			System.out.println("Message unlocked: destID=[" + id + "] / Msg=[" + seq + "]");
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * We have a table with many items that are 'msgs to post'.
	 * 
	 * The hash key is the destination
	 * 
	 * For each destination we may have 0 or more messages to send:
	 * 
	 * Table MSG: hash = destination / range = seq (readable timestamp when the message was added to DB)
	 */
	static void processPosting() {
		List<Item> allDestinations = HootDB.DESTs.getAll();
		for (Item dest : allDestinations) {
			processDestination(dest.getString("id"), dest.getString("url"));
			// System.out.println("Destination processed: " + dest.toJSON());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static void processDestination(String id, String urlToForwardMessagesTo) {
		// get ordered messages to send
		List<Map<String, Object>> msgs = HootDB.MSGs.queryEx(id, true);
		if (msgs.isEmpty())
			return;

		// a message has 2 states:
		// - new
		// - sending (locked)

		// try to lock the first msg-item, effectively locking the entire list of subsequent messages (ordered by insertion timestamp)
		Map<String, Object> firstMsg = msgs.get(0);
		if (!lock(id, firstMsg))
			return; // another server/thread must be checking this destination

		// ok, we locked this first message - other nodes won't be able to lock it and will process the next destination
		// it is important to keep these messages on DB while the loop runs... so no other node will start processing this destination
		if (!postMessageToDestination(id, firstMsg))
			return; // do not send next messages to avoid breaking order

		// ok, we successfully sent the first message!
		// if we have a next message to send, we should lock it before deleting the just-sent message, or else another thread could lock it
		Object msgToDelete = firstMsg.get("seq");

		// the list is ordered - the first item has the smallest timestamp and should be sent next
		for (int i = 1; i < msgs.size(); i++) {
			// try to lock the next msg-item
			Map<String, Object> msg = msgs.get(i);
			boolean locked = lock(id, msg);
			if (!locked)
				break;

			// ok, we locked the next message... we can delete the already sent message
			deleteLastSentMessage(id, msgToDelete);
			msgToDelete = null;

			// try posting the next message
			if (!postMessageToDestination(id, msg))
				break; // do not send next messages to avoid breaking order

			// ok, msg sent
			msgToDelete = msg.get("seq");
		}

		if (msgToDelete != null)
			deleteLastSentMessage(id, msgToDelete);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static void deleteLastSentMessage(String id, Object msgToDelete) {
		// remove sent message:
		HootDB.MSGs.deleteItem(id, msgToDelete);
		System.out.println("Message sent and deleted:\n - id: " + id + "\n - seq: " + msgToDelete);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static boolean postMessageToDestination(String id, Map<String, Object> msg) {
		String destURL = (String) msg.get("url");
		String msgBody = (String) msg.get("body");
		String contentType = (String) msg.get("type");

		try {
			// send to url
			String response = IO.post(destURL, msgBody, contentType);
			if (response != null) {
				// ok, message sent and 200 was returned
				System.out.println("Message sent: " + msgBody + "\nResponse: " + response + "\n - id: " + id + "\n - url: " + destURL);
				return true;
			}
		}
		catch (Exception e) {
			System.out.println("Message could NOT be sent: " + msgBody + "\n - dest-id: " + id + "\n - url: " + destURL);
			System.out.println("Destination will be skipped for now (error while sending message to destinationURL): " + e.toString());
		}

		checkRetryPolicy(id, msg);
		return false;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static boolean lock(String id, Map<String, Object> msg) {
		String state = (String) msg.get("state");
		String urlToForwardMessagesTo = (String) msg.get("url");

		if (state.equals("sending")) {
			System.out.println("Destination skipped (another process is using it):\n - id: " + id + "\n - url: " + urlToForwardMessagesTo);
			return false;	// to keep ordering, let's skip this destination for now
		}

		// state should be 'new' ... lock it (set it to sending)
		try {
			Expected expected = new Expected("state").eq("new");
			HootDB.MSGs.update(id, msg.get("seq"), expected, A.up("state", "sending"));
			return true;
		}
		catch (Exception e) {
			System.out.println("Destination skipped (error while locking it):\n - id: " + id + "\n - url: " + urlToForwardMessagesTo
			  + "\n\nError: " + E.str(e));
			return false; // leaves the loop
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Retry policy - max 24 hrs since message got here. Minimum 3 retries.
	 * 
	 * @param id
	 * @param msg
	 */
	private static void checkRetryPolicy(String id, Map<String, Object> msg) {
		Object seq = msg.get("seq");
		Long cT = G.L(msg, "cT");
		Long expires = cT + 60000;     // 24 * D.HOUR_MILLIS;
		if (System.currentTimeMillis() > expires) {
			// 24 hrs later... expired
			HootDB.MSGs.deleteItem(id, seq);
			System.out.println("Message EXPIRED and deleted:\n - id: " + id + "\n - seq: " + seq);
			return;
		}

		// ok, not expired yet... let's set a 'next-time' for it to be sent:
		long lockedTime;
		int retries = G.i(msg, "attempts", 0);
		if (retries == 0)
			lockedTime = D.MINUTE_MILLIS * 2; // 2 minutes
		else if (retries == 1)
			lockedTime = D.MINUTE_MILLIS * 10; // 10 minutes
		else if (retries == 2)
			lockedTime = D.MINUTE_MILLIS * 45; // 40 mins
		else if (retries == 3)
			lockedTime = D.HOUR_MILLIS * 3; // 3 hours (~4 hours until now)
		else
			lockedTime = D.HOUR_MILLIS * 10; // 10 hours (2 more attempts when get here)

		// TODO remove after tests
		lockedTime = 30000; // 30 sec

		long lockedUntil = System.currentTimeMillis() + lockedTime;
		HootDB.MSGs.update(id, seq, new AttributeUpdate("lockedUntil").put(lockedUntil), new AttributeUpdate("attempts").addNumeric(1));
		System.out.println("Message is going to be retried in " + (lockedTime / 60000) + " minutes..." + A.str(msg));
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

}

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
