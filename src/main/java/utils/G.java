package utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;

/**
 * 'Get' (G) stuff from Maps, Items or other structures.
 */
public class G {

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/** @return list of objects (array) for the specified key. */
	@SuppressWarnings("unchecked")
	public static <T> List<Map<String, T>> list(Map<String, T> i, String key) {
		return (List<Map<String, T>>) i.get(key);
	}

	/** @return list of objects (array) for the specified key. */
	@SuppressWarnings("unchecked")
	public static <T> List<Map<String, T>> list(Item i, String key) {
		return (List<Map<String, T>>) i.get(key);
	}

	/** @return list of objects (array) for the specified key. */
	@SuppressWarnings("unchecked")
	public static <T> List<Map<String, T>> array(Map<String, T> i, String key) {
		return (List<Map<String, T>>) i.get(key);
	}

	/** @return list of objects (array) for the specified key. */
	public static <T> List<Map<String, T>> array(Item i, String key) {
		return i.getList(key);
	}

	/** @return object (map) for the specified key. */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> obj(Map<String, T> i, String key) {
		return (Map<String, T>) i.get(key);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param i - the item to retrieve a boolean from.
	 * @param key - the key of the boolean value.
	 * @return FALSE if value not found, or the boolean value corresponding to the key.
	 */
	public static Boolean bool(Map<String, Object> i, String key) {
		Object val = i.get(key);
		return val == null ? Boolean.FALSE : (Boolean) val;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Get an 'int' from an Item.
	 * 
	 * @return the integer value inside the given item. Returns 'defaultValue' if not found, and returns zero if no defaultValue specified.
	 */
	public static int i(Item item, String key, Integer... defaultValue) {
		Object object = item.get(key);
		if (object instanceof Number)
			return ((Number) object).intValue();
		if (object instanceof String)
			return Integer.parseInt((String) object);
		return defaultValue.length > 0 ? defaultValue[0] : 0;
	}

	/**
	 * Get an 'int' from an Item.
	 * 
	 * @return the integer value inside the given item. Returns 'defaultValue' if not found, and returns zero if no defaultValue specified.
	 */
	public static int i(Map<String, ?> map, String key, Integer... defaultValue) {
		Object object = map.get(key);
		if (object instanceof Number)
			return ((Number) object).intValue();
		if (object instanceof String)
			return Integer.parseInt((String) object);
		return defaultValue.length > 0 ? defaultValue[0] : 0;
	}

	/**
	 * Get a 'long' from an Item.
	 * 
	 * @return the long value inside the given item. Returns 'defaultValue' if not found, and returns zero if no defaultValue specified.
	 */
	public static long L(Item item, String key, long... defaultValue) {
		Object object = item.get(key);
		if (object instanceof Number)
			return ((Number) object).longValue();
		if (object instanceof String)
			return Long.parseLong((String) object);
		return defaultValue.length > 0 ? defaultValue[0] : 0;
	}

	/**
	 * Get a 'long' from an Item.
	 * 
	 * @return the long value inside the given item. Returns 'defaultValue' if not found, and returns zero if no defaultValue specified.
	 */
	public static long L(Map<String, ?> map, String key, long... defaultValue) {
		Object object = map.get(key);
		if (object instanceof Number)
			return ((Number) object).longValue();
		if (object instanceof String)
			return Long.parseLong((String) object);
		return defaultValue.length > 0 ? defaultValue[0] : 0;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Get a BigDecimal from a map.
	 * 
	 * @param item
	 * @param key
	 * @return the BigDecimal value corresponding to 'key', or NULL if not found.
	 */
	public static BigDecimal big(Map<String, ?> item, String key) {
		Object object = item.get(key);
		if (object == null)
			return null;
		if (object instanceof BigDecimal)
			return (BigDecimal) object;
		if (object instanceof Number)
			return BigDecimal.valueOf(((Number) object).doubleValue());

		// err
		System.out.println(B.str("Trying to get a BigDecimal for key = '", key, "', but the value is a '", object.getClass().toString(),
		  "'\nMap:", A.str(item)));
		return null;
	}

	/**
	 * Get a BigDecimal from a map.
	 * 
	 * @param item
	 * @param key
	 * @return the BigDecimal value corresponding to 'key', or NULL if not found.
	 */
	public static BigDecimal big(Item item, String key) {
		Object object = item.get(key);
		if (object == null)
			return null;
		if (object instanceof BigDecimal)
			return (BigDecimal) object;
		if (object instanceof Number)
			return BigDecimal.valueOf(((Number) object).doubleValue());

		// err
		System.out.println(B.str("Trying to get a BigDecimal for key = '", key, "', but the value is a '", object.getClass().toString(),
		  "'\nMap:", A.str(item)));
		return null;
	}

	/**
	 * Get a BigDecimal from a map.
	 * 
	 * @param item
	 * @param key
	 * @param def - the number to return if the value is not found in item.
	 * @return the BigDecimal value corresponding to 'key', or NULL if not found.
	 */
	public static BigDecimal bigOrDefault(Map<String, ?> item, String key, Number def) {
		BigDecimal n = big(item, key);
		if (n != null)
			return n;
		if (def instanceof BigDecimal)
			return (BigDecimal) def;
		return BigDecimal.valueOf(def.doubleValue());
	}

	/**
	 * Get a BigDecimal from a map.
	 * 
	 * @param item
	 * @param key
	 * @param def - the number to return if the value is not found in item.
	 * @return the BigDecimal value corresponding to 'key', or NULL if not found.
	 */
	public static BigDecimal bigOrDefault(Item item, String key, Number def) {
		BigDecimal n = big(item, key);
		if (n != null)
			return n;
		if (def instanceof BigDecimal)
			return (BigDecimal) def;
		return BigDecimal.valueOf(def.doubleValue());
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Gets an internal object inside the param object, starting from 2 levels. Returns null if any of the objects in the chain does not
	 * exist. If a value in the chain exist but is NOT a map, an error is logged, and NULL returned (no errors are thrown).
	 * 
	 * <pre>
	 * var obj = {
	 *   obj1: {
	 *     obj2: {
	 *       obj3: {
	 *         		
	 *       }
	 *     }
	 *   },
	 *   obj4: [  -- note this is an array now...
	 *     obj5: {
	 *       obj6: {
	 *         		
	 *       }
	 *     }
	 *   ]
	 * }
	 * 
	 * To get 'obj2', you would call _G.get(obj, 'obj1', 'obj2');<br>
	 * To get 'obj3', you would call _G.get(obj, 'obj1', 'obj2', 'obj3'); ... and so on!
	 * To get 'obj5', you would call _G.get(obj, 'obj4', 0); ... use the index since obj4 is an array 
	 * To get 'obj6', you would call _G.get(obj, 'obj4', 0, 'obj6'); ... and so on!
	 * </pre>
	 * 
	 * @return object (map) for the specified key.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Item i, String firstLevelKey, Object secondLevelKey, Object... additionalKeys) {
		try {
			Object first = i.get(firstLevelKey);
			return (T) getInternal(secondLevelKey, first, additionalKeys);
		}
		catch (ClassCastException e) {
			String log = "Error in _G.get()\nAn internal value was not an object (map)";
			logErr4GetInternal(i.toJSONPretty(), firstLevelKey, secondLevelKey, log, e, additionalKeys);
		}
		catch (Exception e) {
			String log = "Error in _G.get() - " + e.getMessage();
			logErr4GetInternal(i.toJSONPretty(), firstLevelKey, secondLevelKey, log, e, additionalKeys);
		}
		return null;
	}

	/**
	 * Gets an internal object inside the param object, starting from 2 levels. Returns null if any of the objects in the chain does not
	 * exist. If a value in the chain exist but is NOT a map, an error is logged, and NULL returned (no errors are thrown).
	 * 
	 * <pre>
	 * var obj = {
	 *   obj1: {
	 *     obj2: {
	 *       obj3: {
	 *         		
	 *       }
	 *     }
	 *   },
	 *   obj4: [  -- note this is an array now...
	 *     obj5: {
	 *       obj6: {
	 *         		
	 *       }
	 *     }
	 *   ]
	 * }
	 * 
	 * To get 'obj2', you would call _G.get(obj, 'obj1', 'obj2');<br>
	 * To get 'obj3', you would call _G.get(obj, 'obj1', 'obj2', 'obj3'); ... and so on!
	 * To get 'obj5', you would call _G.get(obj, 'obj4', 0); ... use the index since obj4 is an array 
	 * To get 'obj6', you would call _G.get(obj, 'obj4', 0, 'obj6'); ... and so on!
	 * </pre>
	 * 
	 * @return object (map) for the specified key.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Map<String, Object> i, String firstLevelKey, Object secondLevelKey, Object... additionalKeys) {
		try {
			Object first = i.get(firstLevelKey);
			return (T) getInternal(secondLevelKey, first, additionalKeys);
		}
		catch (ClassCastException e) {
			String log = "Error in _G.get()\nAn internal value was not an object (map)";
			logErr4GetInternal(A.str(i), firstLevelKey, secondLevelKey, log, e, additionalKeys);
		}
		catch (Exception e) {
			String log = "Error in _G.get() - " + e.getMessage();
			logErr4GetInternal(A.str(i), firstLevelKey, secondLevelKey, log, e, additionalKeys);
		}
		return null;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	@SuppressWarnings("rawtypes")
	private static Object getInternal(Object secondLevelKey, Object first, Object... additionalKeys) {
		Object second;
		if (secondLevelKey instanceof Number)
			second = ((List) first).get(((Number) secondLevelKey).intValue());
		else
			second = ((Map) first).get(secondLevelKey.toString());

		if (additionalKeys.length == 0)
			return second;

		Object next = second;
		int j = 0;
		for (; j < additionalKeys.length - 1; j++) {
			Object key = additionalKeys[j];
			if (key instanceof Number)
				next = ((List) next).get(((Number) key).intValue());
			else
				next = ((Map) next).get(key.toString());
		}

		Object key = additionalKeys[j];
		if (key instanceof Number)
			return ((List) next).get(((Number) key).intValue());
		return ((Map) next).get(key.toString());
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static void logErr4GetInternal(String objectSearchedAsJSON, Object firstLevelKey, Object secondLevelKey, String log, Exception e,
	  Object... additionalKeys) {
		String keysSearched = firstLevelKey + "." + secondLevelKey;
		for (int j = 0; j < additionalKeys.length; j++)
			keysSearched += "." + additionalKeys[j];
		System.out.println(B.str(log, "\n - Keys searched: ", keysSearched, "\n - Object searched: " + objectSearchedAsJSON, "\n\nError: ",
		  E.str(e)));
	}
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
