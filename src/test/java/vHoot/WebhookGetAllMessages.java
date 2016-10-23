package vHoot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import utils.A;
import utils.IO;

import com.amazonaws.util.json.Jackson;

public class WebhookGetAllMessages {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		String list = IO.getJSON("http://localhost:80/hoot/dests/");
		List<Map<String, Object>> dests = Jackson.fromJsonString(list, List.class);
		for (Map<String, Object> dest : dests) {
			Object id = dest.get("id");
			System.out.println("\n\nMessages for DESTINATION: " + id);
			try {
				String json = IO.getJSON("http://localhost:80/hoot/dests/" + id + "/messages");
				if (json != null) {
					List<Map<String, Object>> msgs = Jackson.fromJsonString(json, List.class);
					if (msgs.isEmpty())
						System.out.println("\tNo messages");
					else {
						for (Map<String, Object> msg : msgs) {
							System.out.println("\t[" + msg.get("state") + "] - '" + msg.get("seq") + "' - " + A._str(msg));
						}
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
