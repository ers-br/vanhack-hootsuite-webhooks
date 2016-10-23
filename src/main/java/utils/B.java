package utils;

/**
 * B for Buffers. Use this class to build strings using ThreadLocal Buffers (or provided buffers).
 * 
 * <ul>
 * <li>'str' methods: clear the buffer, append parameters and return the resulting String.
 * <li>'buff' methods: clear the buffer, append parameters and return the buffer used.
 * <li>'app' methods: append - DO NOT clear the buffer, append parameters and return the buffer used.
 * </ul>
 */
public class B {

	private static ThreadLocal<StringBuilder> stringBuilder = new ThreadLocal<StringBuilder>() {
		protected StringBuilder initialValue() {
			return new StringBuilder(1024);
		}
	};

	public static StringBuilder buff() {
		StringBuilder b = stringBuilder.get();
		b.setLength(0); // clear/reset the buffer
		return b;
	}

	public static StringBuilder buffNoReset() {
		return stringBuilder.get();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static String str(Iterable<? extends Object> list, String separator) {
		StringBuilder b = stringBuilder.get();
		b.setLength(0); // clear/reset the buffer
		for (Object s : list)
			b.append(s).append(separator);
		int len = b.length();
		if (len > 0) // remove the last separator
			b.setLength(len - separator.length());
		return b.toString();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static String _str(Object a1, Object a2) {
		return buff().append(a1).append(a2).toString();
	}

	public static String _str(Object a1, Object a2, Object a3) {
		return buff().append(a1).append(a2).append(a3).toString();
	}

	public static String _str(Object a1, Object a2, Object a3, Object a4) {
		return buff().append(a1).append(a2).append(a3).append(a4).toString();
	}

	public static String _str(Object a1, Object a2, Object a3, Object a4, Object a5) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).toString();
	}

	public static String _str(Object a1, Object a2, Object a3, Object a4, Object a5, Object a6) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).toString();
	}

	public static String _str(Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).toString();
	}

	public static String _str(Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).toString();
	}

	public static String _str(Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).toString();
	}

	public static String _str(Object a1, Object a2, Object a3, Object a4, Object a5, Object a6, Object a7, Object a8, Object a9, Object a10) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).append(a10).toString();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String str(CharSequence a1, CharSequence a2) {
		return buff().append(a1).append(a2).toString();
	}

	public static String str(CharSequence a1, CharSequence a2, CharSequence a3) {
		return buff().append(a1).append(a2).append(a3).toString();
	}

	public static String str(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4) {
		return buff().append(a1).append(a2).append(a3).append(a4).toString();
	}

	public static String str(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).toString();
	}

	public static String str(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).toString();
	}

	public static String str(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).toString();
	}

	public static String str(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7, CharSequence a8) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).toString();
	}

	public static String str(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7, CharSequence a8, CharSequence a9) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).toString();
	}

	public static String str(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7, CharSequence a8, CharSequence a9, CharSequence a10) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).append(a10).toString();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static StringBuilder buff(CharSequence a1) {
		return buff().append(a1);
	}

	public static StringBuilder buff(CharSequence a1, CharSequence a2) {
		return buff().append(a1).append(a2);
	}

	public static StringBuilder buff(CharSequence a1, CharSequence a2, CharSequence a3) {
		return buff().append(a1).append(a2).append(a3);
	}

	public static StringBuilder buff(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4) {
		return buff().append(a1).append(a2).append(a3).append(a4);
	}

	public static StringBuilder buff(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5);
	}

	public static StringBuilder buff(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6);
	}

	public static StringBuilder buff(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7);
	}

	public static StringBuilder buff(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7, CharSequence a8) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8);
	}

	public static StringBuilder buff(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7, CharSequence a8, CharSequence a9) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9);
	}

	public static StringBuilder buff(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7, CharSequence a8, CharSequence a9, CharSequence a10) {
		return buff().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).append(a10);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static StringBuilder buff(StringBuilder buff, CharSequence a1) {
		buff.setLength(0);
		return buff.append(a1);
	}

	public static StringBuilder buff(StringBuilder buff, CharSequence a1, CharSequence a2) {
		buff.setLength(0);
		return buff.append(a1).append(a2);
	}

	public static StringBuilder buff(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3);
	}

	public static StringBuilder buff(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4);
	}

	public static StringBuilder buff(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5);
	}

	public static StringBuilder buff(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6);
	}

	public static StringBuilder buff(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7);
	}

	public static StringBuilder buff(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8);
	}

	public static StringBuilder buff(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8, CharSequence a9) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9);
	}

	public static StringBuilder buff(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8, CharSequence a9, CharSequence a10) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).append(a10);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2) {
		buff.setLength(0);
		return buff.append(a1).append(a2).toString();
	}

	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).toString();
	}

	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).toString();
	}

	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).toString();
	}

	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).toString();
	}

	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).toString();
	}

	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).toString();
	}

	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8, CharSequence a9) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).toString();
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8, CharSequence a9, CharSequence a10) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).append(a10).toString();
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static String str(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8, CharSequence a9, CharSequence a10, CharSequence a11) {
		buff.setLength(0);
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).append(a10).append(a11)
		  .toString();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods. This method has a first param as String
	 * to not be confused with app(StringBuilder, charSeq)
	 */
	public static StringBuilder app(String a1, CharSequence a2) {
		return buffNoReset().append(a1).append(a2);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(CharSequence a1, CharSequence a2, CharSequence a3) {
		return buffNoReset().append(a1).append(a2).append(a3);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4) {
		return buffNoReset().append(a1).append(a2).append(a3).append(a4);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5) {
		return buffNoReset().append(a1).append(a2).append(a3).append(a4).append(a5);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6) {
		return buffNoReset().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7) {
		return buffNoReset().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7, CharSequence a8) {
		return buffNoReset().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7, CharSequence a8, CharSequence a9) {
		return buffNoReset().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5, CharSequence a6,
	  CharSequence a7, CharSequence a8, CharSequence a9, CharSequence a10) {
		return buffNoReset().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).append(a10);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/** Appends to the buffer the string. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1) {
		return buff.append(a1);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1, CharSequence a2) {
		return buff.append(a1).append(a2);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3) {
		return buff.append(a1).append(a2).append(a3);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4) {
		return buff.append(a1).append(a2).append(a3).append(a4);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5) {
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6) {
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7) {
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8) {
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8, CharSequence a9) {
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9);
	}

	/** Appends to the buffer all the strings. No resetting the buffer like 'str' or 'buff()' methods */
	public static StringBuilder app(StringBuilder buff, CharSequence a1, CharSequence a2, CharSequence a3, CharSequence a4, CharSequence a5,
	  CharSequence a6, CharSequence a7, CharSequence a8, CharSequence a9, CharSequence a10) {
		return buff.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).append(a7).append(a8).append(a9).append(a10);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

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
