package utils;

/**
 * E for Error and Exception utilities.
 */
public class E {

	/**
	 * Returns a String representing the error.
	 * 
	 * @param msg - a message to prepend to the exception.
	 * @param e - the exception.
	 * @param buff - the buffer to use (will be resetted first inside this method).
	 * @return a String representing the error, including the exception stack-trace.
	 */
	public static String exception(String msg, Throwable e, StringBuilder buff) {
		B.buff(buff, msg, "\n", e.toString(), "\n");
		getStackTrace(buff, e.getStackTrace());
		return buff.toString();
	}

	/**
	 * Returns a String representing the error.
	 * 
	 * @param msg - a message to prepend to the exception.
	 * @param e - the exception.
	 * @return a String representing the error, including the exception stack-trace.
	 */
	public static String exception(String msg, Throwable e) {
		return exception(msg, e, new StringBuilder(msg.length() + 256));
	}

	/**
	 * Returns a String representing the error.
	 * 
	 * @param e - the exception.
	 * @param buff - the buffer to use (will be resetted first inside this method).
	 * @return a String representing the error, including the exception stack-trace.
	 */
	public static String exception(Throwable e, StringBuilder buff) {
		B.buff(buff, e.toString(), "\n");
		getStackTrace(buff, e.getStackTrace());
		return buff.toString();
	}

	/**
	 * Returns a String representing the error.
	 * 
	 * @param e - the exception.
	 * @return a String representing the error, including the exception stack-trace.
	 */
	public static String exception(Throwable e) {
		return exception(e, new StringBuilder(256));
	}

	/**
	 * Returns a String representing the error.
	 * 
	 * @param e - the exception.
	 * @return a String representing the error, including the exception stack-trace.
	 */
	public static String str(Throwable e) {
		return exception(e, new StringBuilder(256));
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static StringBuilder getStackTrace(StringBuilder buff, StackTraceElement[] elements) {
		if (elements == null) {
			buff.append("\n... no stack trace ...");
			return buff;
		}

		for (int i = 0; i < elements.length; i++) {
			String line = elements[i].toString();
			buff.append("\n > ").append(line);
		}

		return buff;
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
