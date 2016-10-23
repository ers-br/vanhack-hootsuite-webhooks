package utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.document.Item;

/**
 * MyMap - Wrapper for Maps or Items (DynamoDB v2).
 */
@SuppressWarnings("unchecked")
public class MM {

	public Map<String, Object> map;

	public MM() {
	}

	public MM(Map<String, Object> map) {
		this.map = map;
	}

	public MM(Object map) {
		this((Map<String, Object>) map);
	}

	/**
	 * Creates a MM with the internal map as the same ref as the internal map of the provided item, using reflection to gain access.
	 * 
	 * @param item
	 */
	public MM(Item item) {
		map = map(item);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Redefines the internal map as the same ref as the internal map of the provided item, using reflection to gain access.
	 * 
	 * @param item
	 * @return the internal map of the item (now also internal map of this MM).
	 */
	public Map<String, Object> setup(Item item) {
		return map = map(item);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @return a string value (may be an empty string), or NULL.
	 */
	public String get(String key) {
		return (String) map.get(key);
	}

	/**
	 * @return a string value (may be an empty string), or NULL. The 'Object' key is converted to String before used as a key.
	 */
	public String get(Object key) {
		return (String) map.get(key.toString());
	}

	/**
	 * @return a NON-empty string value, or NULL (returns NULL even if the internal String is an empty String).
	 */
	public String gett(String key) {
		String s = (String) map.get(key);
		return s == null || s.isEmpty() ? null : s;
	}

	/**
	 * @return the integer part of the number stored under 'key', or ZERO if no value defined.
	 */
	public int intOrZero(String key) {
		Object object = map.get(key);
		if (object == null)
			return 0;
		return ((Number) object).intValue();
	}

	/**
	 * @return the value mapped by 'key', as an 'int'.
	 */
	public int i(String key) {
		return ((Number) map.get(key)).intValue();
	}

	/**
	 * @return the value mapped by 'key', as a 'long'.
	 */
	public long l(String key) {
		return ((Number) map.get(key)).longValue();
	}

	/**
	 * @return the value mapped by 'key', as an Integer.
	 */
	public Integer I(String key) {
		return Integer.valueOf(((Number) map.get(key)).intValue());
	}

	/**
	 * @return the value mapped by 'key', as a Long.
	 */
	public Long L(String key) {
		return Long.valueOf(((Number) map.get(key)).longValue());
	}

	/**
	 * @return the value mapped by 'key', as an 'int', or the default value if no value is mapped.
	 */
	public int intOrDef(String key, int def) {
		Object object = map.get(key);
		if (object == null)
			return def;
		return ((Number) object).intValue();
	}

	/**
	 * @return the value mapped by 'key', as a 'BigDecimal'.
	 */
	public BigDecimal bigDecimal(String key) {
		return (BigDecimal) map.get(key);
	}

	/**
	 * @return the value mapped by 'key', as a Number.
	 */
	public Number number(String key) {
		return (Number) map.get(key);
	}

	/**
	 * @return the value mapped by 'key', as a Map<String, Object>.
	 */
	public Map<String, Object> obj(String key) {
		return (Map<String, Object>) map.get(key);
	}

	/**
	 * @return the value mapped by 'key', as a List.
	 */
	public List<?> list(String key) {
		return (List<Object>) map.get(key);
	}

	public List<Map<String, Object>> listOfObjects(String key) {
		return (List<Map<String, Object>>) list(key);
	}

	/**
	 * @return the value mapped by 'key', as a Set of Strings.
	 */
	public Set<String> set(String key) {
		return (Set<String>) map.get(key);
	}

	/**
	 * @return the value mapped by 'key', as a Set of Numbers.
	 */
	public Set<Number> setOfNumbers(String key) {
		return (Set<Number>) map.get(key);
	}

	/**
	 * @return TRUE if there is an attribute named 'key' and its value is a boolean 'TRUE'.
	 */
	public boolean bool(String key) {
		Object object = map.get(key);
		return object != null && ((Boolean) object).booleanValue();
	}

	/**
	 * @param key
	 * @return TRUE if there's a non null value mapped by 'key'.
	 */
	public boolean hasValueFor(String key) {
		return map.get(key) != null;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public String toString() {
		return A.str(map);
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

	/**
	 * Get access to the internal map of the provided item, or calls item.asMap() to get a new map with the values if couldn't use reflection.
	 * 
	 * @param item
	 * @return the internal map of the item, or a new shallow copy of it.
	 */
	public static Map<String, Object> map(Item item) {
		try {
			Field f = item.getClass().getDeclaredField("attributes");
			f.setAccessible(true);
			return (Map<String, Object>) f.get(item);
		}
		catch (Exception e) {
			System.out.println("Error getting internal map!" + E.str(e));
			return item.asMap();
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
