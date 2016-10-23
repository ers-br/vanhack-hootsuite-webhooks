package vHoot;

import java.io.IOException;

import utils.IO;

public class WebhookDeleteDestination {

	public static void main(String[] args) {

		args = new String[] { "20161023@16:56:26+245", "20161023@16:48:27+523", "20161023@16:56:26+307", "20161023@16:56:26+354" };

		for (int i = 0; i < args.length; i++) {
			String id = args[i];

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
