package vHoot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import utils.IO;

import com.amazonaws.util.json.Jackson;

public class WebhookDeleteAllDestinations {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		String list = IO.getJSON("http://localhost:80/hoot/dests/");
		List<Map<String, Object>> dests = Jackson.fromJsonString(list, List.class);
		for (Map<String, Object> dest : dests) {
			Object id = dest.get("id");
			try {
				String json = IO.delete("http://localhost:80/hoot/dests/" + id);
				if (json != null)
					System.out.println("Destination deleted: " + json);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
