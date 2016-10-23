package hoot;

import java.util.List;
import java.util.Map;

import utils.A;
import utils.D;
import utils.M;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;

public class HootDestinations {

	private static final boolean TWO_DESTS_CAN_HAVE_SAME_URL = false;

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static Object insertNew(Item urlData) {
		String url = urlData.getString("url");
		checkURL(url);

		// the ID is going to be a 'readable creation date'
		long currentTimeMillis = System.currentTimeMillis();
		String destID = D.readableTimestamp(currentTimeMillis);

		// create destination in DB
		Item dbItem = new Item();
		dbItem.with("url", url);
		dbItem.with("id", destID);
		HootDB.DESTs.insertNewItem(dbItem);
		System.out.println("New destination: " + dbItem.toJSON());

		// return the new item
		return dbItem.asMap();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static void deleteByID(String id) {
		// deletes the destination: DB item
		Item dest = getDestinationByID(id);

		// delete destination-item from DB:
		HootDB.DESTs.deleteItem(id);

		// do not delete all pending messages ... let them be sent (they have the url inside)
		// DB_MSG.deleteAll(id);
		System.out.println("Deleted destination: " + dest.toJSON());
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static void sendMessage(String destinationID, String body, String type) {
		Item item = getDestinationByID(destinationID);

		// get the final destination of the message (url where to send the body/content-type)
		String destinationURL = item.getString("url");
		if (destinationURL == null)
			throw new RuntimeException("Invalid destination... no destination URL was setup!");

		// insert in DB, ordered by range-key (created-at UTC timestamp)
		Item msgItem = A.item("id", destinationID, "body", body, "type", type, "state", "new");

		// put url inside each msg so the destination item can be deleted before all it's messages are sent
		msgItem.with("url", destinationURL);

		// insert in DB
		HootDB.MSGs.insertNewItemRetrySEQ(msgItem, 20);

		// ok, added to our fake 'queue'
		System.out.println("Added Message to DB - Destination [" + destinationID + "] - Message: " + msgItem.toJSONPretty());
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static Item getDestinationByID(String id) {
		Item item = HootDB.DESTs.getItem(id);
		if (item == null)
			throw new RuntimeException("Destination not found: " + id);
		return item;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static void checkURL(String url) {
		if (url == null || url.isEmpty())
			throw new RuntimeException("URL in not valid: " + url);

		if (!url.startsWith("http://") && !url.startsWith("https://"))
			throw new RuntimeException("Invalid URL protocol: " + url);

		// avoid looping (URL that gets here again)
		// if (url.startsWith("http://localhost") || url.startsWith("https://localhost"))
		// throw new RuntimeException("Invalid URL - localhost not permitted: " + url);
		// if (url.startsWith("http://127.0.0.1") || url.startsWith("https://127.0.0.1"))
		// throw new RuntimeException("Invalid URL - 127.0.0.1 not permitted: " + url);
		// TODO ... other possibilities

		// check if the URL must be unique in the DESTs DB
		if (!TWO_DESTS_CAN_HAVE_SAME_URL) {
			ScanSpec scan = new ScanSpec();
			scan.withFilterExpression("#url = :u");
			scan.withNameMap(M.smap("#url", "url"));
			scan.withValueMap(M.map(":u", url));
			List<Map<String, Object>> msgs = HootDB.DESTs.scanEx(scan);
			if (msgs != null && !msgs.isEmpty())
				throw new RuntimeException("URL already registered!");
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static List<Map<String, Object>> listAll() {
		return HootDB.DESTs.scanEx(new ScanSpec());
	}

	public static Object listAllMessages(String destID) {
		return HootDB.MSGs.queryEx(destID, false);
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
