package hoot;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import utils.D;
import db.DB;

public class HootApp {

	public static void main(String[] args) throws Exception {
		int port, dynamoDBLocalPort = 8010;
		try {
			// check selected port and DynamoDB-local port (both optional):
			port = args.length > 0 ? Integer.parseInt(args[0]) : 80;
			if (args.length > 1)
				dynamoDBLocalPort = Integer.parseInt(args[1]);
		}
		catch (Exception e) {
			System.out.println("Error in arguments... Usage: <port> <dynamoDBLocal-Port>  (both as numbers and optional)");
			return;
		}

		// create the api-server
		long startTime = System.currentTimeMillis();
		System.out.print("Creating server instance on port: " + port + " ... ");
		Server server = createServer(port);
		System.out.println("[OK] Created in " + D.diffTimeStr(startTime));

		// listener start
		startTime = System.currentTimeMillis();
		System.out.print("Starting server instance on port: " + port + " ... ");
		server.start();
		System.out.println("[OK] Started in " + D.diffTimeStr(startTime));

		// database init
		startTime = System.currentTimeMillis();
		System.out.print("Starting DB connector on port: " + dynamoDBLocalPort);
		HootDB.init(dynamoDBLocalPort);
		System.out.println(" ... [OK] DB initialized in " + D.diffTimeStr(startTime)
		  + "\n-----------------------------------------------------------------------");

		// start the sender thread
		HootSender.init();

		// let's make this thread (main) wait here until the server ends...
		server.join();
		System.out.print("Server going down...");

		// server stopped! Release resources...
		DB.shutdown();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	private static Server createServer(int port) {
		// ////////////////////////////////////////
		// criar server com thread pool especial !
		Server jettyServer = new Server();
		jettyServer.setStopTimeout(D.MINUTE_MILLIS);

		// http-connector:
		ServerConnector connector = new ServerConnector(jettyServer);
		connector.setPort(port);
		jettyServer.addConnector(connector);

		// server filter
		ServletHandler handler = new ServletHandler();
		handler.addFilterWithMapping(HootFilter.class, "/*", EnumSet.of(DispatcherType.ASYNC, DispatcherType.REQUEST));
		jettyServer.setHandler(handler);

		return jettyServer;
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
