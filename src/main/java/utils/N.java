package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Number utilities.
 */
public final class N {

	public static final BigDecimal ZERO = BigDecimal.valueOf(0);
	public static final BigDecimal TEN = new BigDecimal("10.00");
	public static final BigDecimal HUNDRED = new BigDecimal("100.00");
	public static final BigDecimal THOUSAND = new BigDecimal("1000.00");
	public static final BigDecimal TEN_THOUSEND = new BigDecimal("10000.00");

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	private static ThreadLocal<NumberFormat> thousandSepsBuilder = new ThreadLocal<NumberFormat>() {
		protected NumberFormat initialValue() {
			NumberFormat f = DecimalFormat.getInstance();
			f.setMinimumFractionDigits(0);
			f.setMaximumFractionDigits(2);
			f.setGroupingUsed(true);
			return f;
		}
	};

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String getTotalBytesThousandsSeps(long size) {
		return thousandSepsBuilder.get().format(size);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String getKB(long size) {
		int kb = (int) (size / 1024.0);
		return Integer.toString(kb) + " KB";
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String getMB(long size) {
		int mb = (int) (size / (1024.0 * 1024));
		return Integer.toString(mb) + " MB";
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String getGB(long size) {
		int gb = (int) (size / (1024.0 * 1024 * 1024));
		return Integer.toString(gb) + " GB";
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String byteSize(long total) {
		return byteSize(total, true);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String byteSize(long total, boolean groupBy_KB_MB_GB) {
		if (!groupBy_KB_MB_GB)
			return thousandSepsBuilder.get().format(total);
		if (total >= 1024 * 1024 * 1024)
			return getGB(total);
		if (total >= 1024 * 1024)
			return getMB(total);
		if (total >= 1024)
			return getKB(total);
		return total + " bytes";
	}

	/**
	 * String byte size with at most 4 chars in length.
	 * <ul>
	 * <li>9999
	 * <li>999K
	 * <li>999M
	 * <li>2G (maximum, as per Java int size)
	 * <ul>
	 * 
	 * @param size
	 * @return byte size as String, but with max length = 4 chars... 9999, 999K, 999M, up to 2G (Java int size)
	 */
	public static String byteSizeCompact(int size) {
		if (size <= 9999)
			return Long.toString(size);

		long kb = size / 1024;
		if (kb <= 999)
			return kb + "K";

		long mb = size / (1024 * 1024);
		if (mb <= 999)
			return mb + "M";

		long gb = size / (1024 * 1024 * 1024);
		return gb + "G";
	}

	/**
	 * String byte size with at most 4 chars in length (it gets bigger than 4 chars if size > 999T)
	 * <ul>
	 * <li>9999
	 * <li>999K
	 * <li>999M
	 * <li>999G
	 * <li>999T
	 * <ul>
	 * 
	 * @param size
	 * @return byte size as String, but with max length = 4 chars... 9999, 999K, 999M, 999G, 999T, etc
	 */
	public static String byteSizeCompact(long size) {
		if (size <= 9999)
			return Long.toString(size);

		long kb = size / 1024;
		if (kb <= 999)
			return kb + "K";

		long mb = size / (1024 * 1024);
		if (mb <= 999)
			return mb + "M";

		long gb = size / (1024 * 1024 * 1024);
		if (gb <= 999)
			return gb + "G";

		long tb = size / (1024L * 1024L * 1024L * 1024L);
		return tb + "T";
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/** @return number as String with 2 decimals. Ex: '23.56' */
	public static String format2(Number number) {
		return fixed2(number, B.buff()).toString();
	}

	/** @return number as String with 2 decimals. Ex: '23.56' */
	public static String format2(Object number) {
		if (number instanceof Number)
			return fixed2((Number) number, B.buff()).toString();
		return fixed2(new BigDecimal(number.toString()), B.buff()).toString();
	}

	/**
	 * @param number
	 * @param buff - buffer - note that this buffer is not cleared before use. You are responsible for clearing it if this is the case.
	 * @return the buffer, now with a number appended to it, in decimal format, with 2 digits after a '.', like "243.50"
	 */
	public static StringBuilder fixed2(Number number, StringBuilder buff) {
		int val = (int) (number.doubleValue() * 100);
		buff.append(val);
		buff.insert(buff.length() - 2, '.');
		return buff;
	}

	/** @return System.currentTimeMillis() */
	public static long currTime() {
		return System.currentTimeMillis();
	}

	/** @return Long.toString(System.currentTimeMillis()) */
	public static String currTimeSTR() {
		return Long.toString(System.currentTimeMillis());
	}

	/** @return System.currentTimeMillis() / 1000 */
	public static long currTimeSecs() {
		return System.currentTimeMillis() / 1000;
	}

	/** @return Long.toString(System.currentTimeMillis() / 1000) */
	public static String currTimeSecsSTR() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	public static final String str(int num) {
		return Integer.toString(num);
	}

	public static final String str(Integer num) {
		return Integer.toString(num);
	}

	public static final String str(long num) {
		return Long.toString(num);
	}

	public static final String str(Long num) {
		return Long.toString(num);
	}

	public static final String str(double num) {
		return format2(num);
	}

	public static final String str(Number num) {
		if (num instanceof BigDecimal || num instanceof Double || num instanceof Float)
			return format2(num);
		return num.toString();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/** @return TRUE if 'n' is positive (>0). */
	public static boolean gtZero(BigDecimal n) {
		return n.compareTo(BigDecimal.ZERO) > 0;
	}

	/** @return TRUE if 'n' is positive (>0) */
	public static boolean isPositive(BigDecimal n) {
		return n.compareTo(BigDecimal.ZERO) > 0;
	}

	/** @return TRUE if 'n' is 0 or positive. */
	public static boolean geZero(BigDecimal n) {
		return n.compareTo(BigDecimal.ZERO) >= 0;
	}

	/** @return TRUE if 'n' is negative (<0). */
	public static boolean isNegative(BigDecimal n) {
		return n.compareTo(BigDecimal.ZERO) < 0;
	}

	/** @return TRUE if 'n' is 0 or negative (<0). */
	public static boolean leZero(BigDecimal n) {
		return n.compareTo(BigDecimal.ZERO) <= 0;
	}

	/** @return TRUE if 'n' is negative. */
	public static boolean ltZero(BigDecimal n) {
		return n.compareTo(BigDecimal.ZERO) < 0;
	}

	/** @return TRUE if 'a' is greater than 'b' */
	public static boolean a_gt_B(BigDecimal a, BigDecimal b) {
		return a.compareTo(b) > 0;
	}

	/** @return TRUE if 'a' is greater than (or equals) 'b' */
	public static boolean a_ge_B(BigDecimal a, BigDecimal b) {
		return a.compareTo(b) >= 0;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @return valor dividido por 'divisor', usando 2 casas decimais e arredondamento pra cima.
	 */
	public static BigDecimal div(BigDecimal valor, BigDecimal divisor) {
		return valor.divide(divisor, 2, RoundingMode.HALF_UP);
	}

	/**
	 * @return valor dividido por 'divisor', usando 2 casas decimais e arredondamento pra cima.
	 */
	public static BigDecimal div(long valor, BigDecimal divisor) {
		return BigDecimal.valueOf(valor).divide(divisor, 2, RoundingMode.HALF_UP);
	}

	/**
	 * @return valor multiplicado por 'm'.
	 */
	public static BigDecimal mult(BigDecimal valor, long m) {
		return valor.multiply(BigDecimal.valueOf(m));
	}

	/**
	 * @return valor multiplicado por 'm'.
	 */
	public static BigDecimal mult(BigDecimal valor, double m) {
		return valor.multiply(BigDecimal.valueOf(m));
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param intAsString
	 * @return integer corresponding to the string.
	 */
	public static int i(String intAsString) {
		return Integer.parseInt(intAsString);
	}

	/**
	 * @param longAsString
	 * @return long corresponding to the string.
	 */
	public static long l(String longAsString) {
		return Long.parseLong(longAsString);
	}

	/**
	 * @param numberAsString
	 * @return BigDecimal corresponding to the string.
	 */
	public static BigDecimal big(String numberAsString) {
		return new BigDecimal(numberAsString);
	}

	public static BigDecimal big(int val) {
		return BigDecimal.valueOf(val);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

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
