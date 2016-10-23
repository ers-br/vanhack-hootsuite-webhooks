package hoot;

// import hoot.Hoot;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import utils.A;
import utils.E;
import utils.IO;
import utils.M;

import com.amazonaws.services.dynamodbv2.document.Item;

/**
 * Servlet filter that intercepts and handles all requests.
 */
public final class HootFilter implements Filter {

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		final String url = req.getRequestURI();

		Object result = null;
		try {
			if (url.startsWith("/hoot/dests"))
				result = handleDestinations(req, res, url.substring("/hoot/dests".length()));

			if (result == null)
				sendError(req, res, "Method / URL not expected!\n\n " + showHelp());
			else
				sendResponse(req, res, result);
		}
		catch (Exception e) {
			System.out.println("Error: " + E.str(e));
			sendError(req, res, e.toString());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// URL: /hoot/dests

	private static Object handleDestinations(HttpServletRequest req, HttpServletResponse res, String remainingURI) throws IOException {
		if (!remainingURI.isEmpty() && !remainingURI.equals("/") && remainingURI.substring(1).split("/").length > 1)
			return handleMessagesToADestination(req, res, remainingURI.substring(1));

		// check which API method is it
		switch (req.getMethod()) {
			case "POST":
				return insertNewDestination(req, res);
			case "GET":
				return HootDestinations.listAll();
			case "DELETE":
				return deleteDestination(req, res, remainingURI); // DELETE /hoot/dests/123
		}
		return null;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static Object handleMessagesToADestination(HttpServletRequest req, HttpServletResponse res, String remainingURL)
	  throws IOException {

		String[] data = remainingURL.split("/", 2);
		if (data.length < 2 || !data[1].equals("messages"))
			throw new RuntimeException("Invalid URL: " + req.getRequestURI() + "\n\n" + showHelp());

		// destination ID from URL:
		String destID = data[0];

		// POST or DELETE
		if (!req.getMethod().equals("POST")) {
			if (req.getMethod().equals("GET"))
				return HootDestinations.listAllMessages(destID);
			return null;
		}

		// ok, read body
		Item msg = jsonBody(req);
		String body = msg.getString("msg-body");
		String type = msg.getString("content-type");
		if (destID == null || destID.isEmpty())
			throw new RuntimeException("'id' (destination-id) must be provided to send a message");
		if (body == null || body.isEmpty())
			throw new RuntimeException("'msg-body' must be provided to send a message");
		if (type == null || type.isEmpty())
			throw new RuntimeException("'content-type' must be provided to send a message");

		// ok, we have necessary data:
		HootDestinations.sendMessage(destID, body, type);
		return "OK";
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static Object insertNewDestination(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// POST /hoot/dests -- body: { url: '' }
		Item urlData = jsonBody(req);
		Object newDest = HootDestinations.insertNew(urlData);
		return newDest;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static Object deleteDestination(HttpServletRequest req, HttpServletResponse res, String remainingURL) {
		String id = remainingURL.substring(1); // '/12' -> '12'
		HootDestinations.deleteByID(id);
		return "OK";
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static void sendResponse(HttpServletRequest req, HttpServletResponse res, Object result) throws IOException {
		if (result instanceof String)
			sendJSON(req, res, (String) result);
		else
			sendJSON(req, res, A.str(result));
	}

	private static void sendPlain(HttpServletRequest req, HttpServletResponse res, String json) throws IOException {
		addDefaultHeaders(res, false, true);
		res.setContentType(IO.TEXT_CONTENT_TYPE);
		res.getWriter().write(json);
	}

	private static void sendJSON(HttpServletRequest req, HttpServletResponse res, String json) throws IOException {
		addDefaultHeaders(res, true, true);
		res.getWriter().write(json);
	}

	private static void sendResponseInsideJSON(HttpServletRequest req, HttpServletResponse res, String responseData) throws IOException {
		addDefaultHeaders(res, true, true);
		res.getWriter().write(A.str(M.m("response", responseData)));
	}

	public static void sendError(HttpServletRequest req, HttpServletResponse res, String errorMessage) throws IOException {
		addDefaultHeaders(res, true, false);
		res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		res.getWriter().write(A.str(M.m("error", errorMessage)));
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static void addDefaultHeaders(HttpServletResponse response, boolean json, boolean addOK) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type");
		if (json)
			response.setContentType(IO.JSON_CONTENT_TYPE);
		if (addOK)
			response.setStatus(HttpServletResponse.SC_OK);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static String showHelp() {
		String help = "Valid API:\n\n";
		help += "POST /hoot/dests  (inserts a new destination using the 'url' field in the JSON body.)\n";
		help += "GET /hoot/dests   (retrieves all destinations)\n";
		help += "POST /hoot/dests/12345/messages  (insert a message to be sent to the URL corresponding to the destination '12345')\n";
		help += "GET /hoot/dests/12345/messages  (lists msgs to be sent to the destination '12345')\n";
		return help;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static Item jsonBody(HttpServletRequest req) throws IOException {
		return Item.fromJSON(IOUtils.toString(req.getInputStream()));
	}
}

// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------

// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------

// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------

// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------

// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------------------------
