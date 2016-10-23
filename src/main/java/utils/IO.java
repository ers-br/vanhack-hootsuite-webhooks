package utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * <pre>
 * External IO for JSON messaging. Use this to execute GETs or POSTs against external JSON APIs.
 * 
 * This class uses <b>Thread-Local</b> variables so they can be safely reused in a thread-pool.
 * 
 * Expected: "-Dfile.encoding=UTF-8"
 * </pre>
 */
public class IO {

	/** JSON content type with charset: "application/json;charset=UTF-8" */
	public static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

	/** XML content type with charset: "application/xml;charset=UTF-8" */
	public static final String XML_CONTENT_TYPE = "application/xml;charset=UTF-8";

	/** HTML content type with charset: "text/html;charset=UTF-8" */
	public static final String HTML_CONTENT_TYPE = "text/html;charset=UTF-8";

	/** Text content type with charset: "text/plain;charset=UTF-8" */
	public static final String TEXT_CONTENT_TYPE = "text/plain;charset=UTF-8";

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// HttpClient - reusable thread local

	private static ThreadLocal<CloseableHttpClient> httpClientBuilder = new ThreadLocal<CloseableHttpClient>() {
		protected CloseableHttpClient initialValue() {
			return HttpClients.createDefault();
		}
	};

	static CloseableHttpClient httpClient() {
		CloseableHttpClient client = httpClientBuilder.get();
		return client;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// HttpGet - reusable thread local

	private static ThreadLocal<HttpGet> httpGetBuilder = new ThreadLocal<HttpGet>() {
		protected HttpGet initialValue() {
			return new HttpGet();
		}
	};

	static HttpGet httpGET(String url) {
		HttpGet get = httpGetBuilder.get();
		get.reset();
		get.setURI(URI.create(url));
		return get;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// HttpGet - reusable thread local

	private static ThreadLocal<HttpDelete> httpDeleteBuilder = new ThreadLocal<HttpDelete>() {
		protected HttpDelete initialValue() {
			return new HttpDelete();
		}
	};

	static HttpDelete httpDELETE(String url) {
		HttpDelete delete = httpDeleteBuilder.get();
		delete.reset();
		delete.setURI(URI.create(url));
		return delete;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// HttpPost - reusable thread local

	private static ThreadLocal<HttpPost> httpJsonPostBuilder = new ThreadLocal<HttpPost>() {
		protected HttpPost initialValue() {
			HttpPost post = new HttpPost();
			return post;
		}
	};

	static HttpPost httpPOST(String url, String data, String contentType) throws UnsupportedEncodingException {
		byte[] data2Send = data.getBytes(StandardCharsets.UTF_8);
		HttpPost post = httpJsonPostBuilder.get();
		post.reset();
		post.setURI(URI.create(url));
		post.setEntity(new ByteArrayEntity(data2Send));
		post.setHeader("Content-Type", contentType);
		return post;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// GET

	/**
	 * Executes a GET against the URL, and expects JSON data as response.
	 * 
	 * @return NULL if status response not 200, or the response body as a string if 200-OK.
	 */
	public static String getJSON(String url) throws IOException {
		HttpGet get = httpGET(url);
		CloseableHttpResponse responseEntity = httpClient().execute(get);
		try {
			HttpEntity entity = responseEntity.getEntity();
			if (responseEntity.getStatusLine().getStatusCode() == 200)
				return IOUtils.toString(entity.getContent());
			return null;
		}
		finally {
			responseEntity.close();
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// POST

	/**
	 * @param url - destination URL.
	 * @param postData - post body.
	 * @param contentType - content-type header.
	 * @return NULL if status response not 200, or the response body as a string if 200-OK.
	 * @throws IOException
	 */
	public static String post(String url, String postData, String contentType) throws IOException {
		HttpPost post = httpPOST(url, postData, contentType);
		CloseableHttpResponse responseEntity = httpClient().execute(post);
		try {
			HttpEntity entity = responseEntity.getEntity();
			if (responseEntity.getStatusLine().getStatusCode() == 200)
				return IOUtils.toString(entity.getContent());
			return null;
		}
		finally {
			responseEntity.close();
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// DELETE

	/**
	 * Executes a DELETE against the URL.
	 * 
	 * @return NULL if status response not 200, or the response body as a string if 200-OK.
	 */
	public static String delete(String url) throws IOException {
		HttpDelete delete = httpDELETE(url);
		CloseableHttpResponse responseEntity = httpClient().execute(delete);
		try {
			HttpEntity entity = responseEntity.getEntity();
			if (responseEntity.getStatusLine().getStatusCode() == 200)
				return IOUtils.toString(entity.getContent());
			return null;
		}
		finally {
			responseEntity.close();
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

}

// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------

// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------

// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------

// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------

// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------------------------
