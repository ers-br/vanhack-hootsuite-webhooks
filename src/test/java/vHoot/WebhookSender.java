package vHoot;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import utils.A;
import utils.IO;
import utils.M;

import com.amazonaws.util.json.Jackson;

public class WebhookSender {

	public static void main(String[] args) throws IOException {
		// get all dests:
		String list = IO.getJSON("http://localhost:80/hoot/dests/");
		List<Map<String, Object>> dests = Jackson.fromJsonString(list, List.class);
		for (Map<String, Object> dest : dests) {
			int random = ThreadLocalRandom.current().nextInt(1, 10);
			Object id = dest.get("id");
			for (int i = 0; i < random; i++)
				sendMessageToDest((String) id);
		}
	}

	private static void sendMessageToDest(String destID) {
		String body = A.str(M.m("txt", "Hello Destination '" + destID + "' ... now is " + new Date()));
		String postData = A.str(M.m("msg-body", body, "content-type", IO.JSON_CONTENT_TYPE));
		try {
			IO.post("http://localhost:" + 80 + "/hoot/dests/" + destID + "/messages", postData, IO.JSON_CONTENT_TYPE);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
