package vHoot;

// import hoot.Hoot;

import hoot.HootFilter;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * Servlet filter that intercepts and handles all requests.
 */
public final class WebhookFilter implements Filter {

	private static int nextWebhookID = 1;

	private String webhookID;

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public void init(FilterConfig filterConfig) throws ServletException {
		synchronized (WebhookFilter.class) {
			webhookID = "[" + nextWebhookID + "] - ";
			nextWebhookID++;
		}
	}

	public void destroy() {
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String msg = IOUtils.toString(req.getInputStream(), req.getCharacterEncoding());
		System.out.println(webhookID + "Got message from dispatcher: " + msg);

		int max = 100;
		int random = ThreadLocalRandom.current().nextInt(0, max);
		if (random > 65) {
			// let's error!
			HootFilter.sendError(req, res, webhookID + "Random ERROR: " + random);
		}
		else {
			HootFilter.sendResponse(req, res, webhookID + "OK, received msg");
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

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
