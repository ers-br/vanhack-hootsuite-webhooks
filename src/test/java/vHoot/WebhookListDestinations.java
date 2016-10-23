package vHoot;

import java.io.IOException;

import utils.IO;

public class WebhookListDestinations {

	public static void main(String[] args) {
		try {
			String json = IO.getJSON("http://localhost:80/hoot/dests");
			System.out.println("Destinations registered:\n" + json);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
