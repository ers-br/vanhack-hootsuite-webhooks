package vHoot;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import utils.A;
import utils.E;
import utils.IO;
import utils.M;

public class WebhookEndpoint {

	public static final String DISPATCHER_URL = "http://localhost:80";

	public static void main(String[] args) {
		// creates many servers ...
		// these are the destinations targeted by our dispatcher
		for (int i = 0; i < 3; i++)
			createEndpoint(Webhook.startingDestinationsPort + i);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static void createEndpoint(int port) {
		Server server = createServer(port);
		try {
			server.start();
			System.out.println("Endpoint listening on port: " + port);

			// register this destination in the server
			String destination = "http://localhost:" + port;
			String jsonToPostToCreateNewDestination = A.str(M.m("url", destination));
			String r = IO.post(DISPATCHER_URL + "/hoot/dests", jsonToPostToCreateNewDestination, IO.JSON_CONTENT_TYPE);
			if (r != null)
				System.out.println("Endpoint registered to receive messages - destination: " + destination + "\n - resp: " + r);
		}
		catch (Exception e) {
			System.out.println("Error while starting endpoint server: " + E.str(e));
			try {
				server.stop();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	private static Server createServer(int port) {
		// ////////////////////////////////////////
		// criar server com thread pool especial !
		Server jettyServer = new Server();
		jettyServer.setStopTimeout(10000);

		// http-connector:
		ServerConnector connector = new ServerConnector(jettyServer);
		connector.setPort(port);
		jettyServer.addConnector(connector);

		// server filter
		ServletHandler handler = new ServletHandler();
		handler.addFilterWithMapping(WebhookFilter.class, "/*", EnumSet.of(DispatcherType.ASYNC, DispatcherType.REQUEST));
		jettyServer.setHandler(handler);

		return jettyServer;
	}

}
