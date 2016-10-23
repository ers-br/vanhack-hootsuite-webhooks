package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * D for 'Date'. General utilities for dates.
 */
public class D {

	/** One minute in ms */
	public static final long MINUTE_MILLIS = 1000L * 60; // 1 segundo * 60 = 1 minuto

	/** One hour in ms */
	public static final long HOUR_MILLIS = MINUTE_MILLIS * 60; // 1 minuto * 60 = 1 hr

	/** One day (24 hrs) in ms */
	public static final long DAY_MILLIS = HOUR_MILLIS * 24; // 1 hr * 24 = 1 dia

	public static final int MINUTE_SECONDS = 60;

	public static final int HOUR_SECONDS = 60 * MINUTE_SECONDS;

	public static final int DAY_SECONDS = 24 * HOUR_SECONDS;

	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------

	private static ThreadLocal<GregorianCalendar> calendar = new ThreadLocal<GregorianCalendar>() {
		protected GregorianCalendar initialValue() {
			return new GregorianCalendar();
		}
	};

	public static GregorianCalendar cal() {
		return calendar.get();
	}

	public static Calendar now() {
		GregorianCalendar cal = calendar.get();
		cal.setTimeInMillis(System.currentTimeMillis());
		return cal;
	}

	public static Calendar cal(Date d) {
		GregorianCalendar cal = calendar.get();
		cal.setTime(d);
		return cal;
	}

	public static Calendar cal(long time) {
		GregorianCalendar cal = calendar.get();
		cal.setTimeInMillis(time);
		return cal;
	}

	public static Calendar calClean() {
		GregorianCalendar cal = calendar.get();
		cal.clear();
		return cal;
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Returns a string representing an elapsed time until NOW, like: '10 ms', '20 secs', etc
	 * 
	 * @param startTime
	 * @return a string representing an elapsed time until NOW, like: '10 ms', '20 secs', etc
	 */
	public static String diffTimeStr(long startTime) {
		return getElapsedTimeString(System.currentTimeMillis() - startTime);
	}

	/**
	 * Returns a string representing the elapsed time provided in ms, like: '10 ms', '20 secs', etc
	 * 
	 * @param timeMillis - elapsed time in ms.
	 * @return a string representing the elapsed time, like: '10 ms', '20 secs', etc
	 */
	public static String getElapsedTimeString(long timeMillis) {
		String timeStr;
		if (timeMillis < 1000)
			timeStr = B.str(Long.toString(timeMillis), " ms");
		else {
			String durationTime = getHumanDurationTimeEx(timeMillis / 1000L);
			timeStr = B.str(durationTime, " + ", Long.toString(timeMillis % 1000), " ms");
		}
		return timeStr;
	}

	/**
	 * Returns a string representing the elapsed time provided in seconds, like: '8 secs', '2 mins', etc
	 * 
	 * @param timeSecs - elapsed time in seconds.
	 * @return a string representing the elapsed time, like: '8 secs', '2 mins', etc
	 */
	public static String getHumanDurationTimeEx(long timeSecs) {
		if (timeSecs < MINUTE_SECONDS)
			return B.str(Long.toString(timeSecs), " seconds");

		long days = timeSecs / DAY_SECONDS;
		timeSecs -= days * DAY_SECONDS;
		long hours = timeSecs / HOUR_SECONDS;
		timeSecs -= hours * HOUR_SECONDS;
		long minutes = timeSecs / MINUTE_SECONDS;
		timeSecs -= minutes * MINUTE_SECONDS;

		String secs = Long.toString(timeSecs);

		if (days > 0)
			return B.str(Long.toString(days), " days, ", Long.toString(hours), " hrs, ", Long.toString(minutes), " mins and ", secs, " secs");

		if (hours > 0)
			return B.str(Long.toString(hours), " hrs, ", Long.toString(minutes), " mins and ", secs, " secs");

		if (minutes > 0)
			return B.str(Long.toString(minutes), " mins ", secs, " secs");

		return B.str(secs, " secs");
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @return formatted date like: '20160316@18:06:55+888'
	 */
	public static String readableTimestamp(long time) {
		return readableTimestamp(B.buff(), cal(time)).toString();
	}

	/**
	 * @return formatted date like: '20160316@18:06:55+888'
	 */
	public static StringBuilder readableTimestamp(StringBuilder buff, Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		int M = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DAY_OF_MONTH);
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);
		int ms = cal.get(Calendar.MILLISECOND);

		buff.append(year);
		if (M < 10)
			buff.append('0');
		buff.append(M);

		if (d < 10)
			buff.append('0');
		buff.append(d).append('-');

		if (h < 10)
			buff.append('0');
		buff.append(h).append('_');

		if (m < 10)
			buff.append('0');
		buff.append(m).append('_');

		if (s < 10)
			buff.append('0');
		buff.append(s).append('+');

		if (ms < 10)
			buff.append("00").append(ms);
		else if (ms < 100)
			buff.append('0').append(ms);
		else
			buff.append(ms);

		return buff;
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
