package utils;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.common.collect.ImmutableMap;

/**
 * Small-print hash map with a string key, fluent interface and helper methods.
 *
 * @param <T> - the type of the values held in this mapping.
 */
public class M<T> extends HashMap<String, T> {

	public static final Map<String, Object> EMPTY_MAP = new ImmutableMap.Builder<String, Object>().build();

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public String toString() {
		return A.str(this);
	}

	public T get(Object key) {
		return super.get(key.toString());
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public M<T> with(String key, T value) {
		super.put(key, value);
		return this;
	}

	public static M<Object> map() {
		return new M<>();
	}

	public static M<String> smap() {
		return new M<>();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static M<Object> map(Map<String, ? extends Object> itemToCopyFrom) {
		M<Object> m = new M<>();
		if (itemToCopyFrom != null)
			m.putAll(itemToCopyFrom);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String attrib1) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, attrib1, m);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String attrib1, String attrib2) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, attrib1, m);
		copyIfPresent(itemToCopyFrom, attrib2, m);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String attrib1, String attrib2, String attrib3) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, attrib1, m);
		copyIfPresent(itemToCopyFrom, attrib2, m);
		copyIfPresent(itemToCopyFrom, attrib3, m);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String attrib1, String attrib2, String attrib3, String attrib4) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, attrib1, m);
		copyIfPresent(itemToCopyFrom, attrib2, m);
		copyIfPresent(itemToCopyFrom, attrib3, m);
		copyIfPresent(itemToCopyFrom, attrib4, m);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String attrib1, String attrib2, String attrib3, String attrib4, String attrib5) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, attrib1, m);
		copyIfPresent(itemToCopyFrom, attrib2, m);
		copyIfPresent(itemToCopyFrom, attrib3, m);
		copyIfPresent(itemToCopyFrom, attrib4, m);
		copyIfPresent(itemToCopyFrom, attrib5, m);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String a1, String a2, String a3, String a4, String a5, String a6) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, a1, m);
		copyIfPresent(itemToCopyFrom, a2, m);
		copyIfPresent(itemToCopyFrom, a3, m);
		copyIfPresent(itemToCopyFrom, a4, m);
		copyIfPresent(itemToCopyFrom, a5, m);
		copyIfPresent(itemToCopyFrom, a6, m);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String a1, String a2, String a3, String a4, String a5, String a6, String a7) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, a1, m);
		copyIfPresent(itemToCopyFrom, a2, m);
		copyIfPresent(itemToCopyFrom, a3, m);
		copyIfPresent(itemToCopyFrom, a4, m);
		copyIfPresent(itemToCopyFrom, a5, m);
		copyIfPresent(itemToCopyFrom, a6, m);
		copyIfPresent(itemToCopyFrom, a7, m);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String a1, String a2, String a3, String a4, String a5, String a6, String a7, String a8) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, a1, m);
		copyIfPresent(itemToCopyFrom, a2, m);
		copyIfPresent(itemToCopyFrom, a3, m);
		copyIfPresent(itemToCopyFrom, a4, m);
		copyIfPresent(itemToCopyFrom, a5, m);
		copyIfPresent(itemToCopyFrom, a6, m);
		copyIfPresent(itemToCopyFrom, a7, m);
		copyIfPresent(itemToCopyFrom, a8, m);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String a1, String a2, String a3, String a4, String a5, String a6, String a7, String a8,
	  String a9) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, a1, m);
		copyIfPresent(itemToCopyFrom, a2, m);
		copyIfPresent(itemToCopyFrom, a3, m);
		copyIfPresent(itemToCopyFrom, a4, m);
		copyIfPresent(itemToCopyFrom, a5, m);
		copyIfPresent(itemToCopyFrom, a6, m);
		copyIfPresent(itemToCopyFrom, a7, m);
		copyIfPresent(itemToCopyFrom, a8, m);
		copyIfPresent(itemToCopyFrom, a9, m);
		return m;
	}

	public static M<Object> map(Item itemToCopyFrom, String a1, String a2, String a3, String a4, String a5, String a6, String a7, String a8,
	  String a9, String a10) {
		M<Object> m = new M<>();
		copyIfPresent(itemToCopyFrom, a1, m);
		copyIfPresent(itemToCopyFrom, a2, m);
		copyIfPresent(itemToCopyFrom, a3, m);
		copyIfPresent(itemToCopyFrom, a4, m);
		copyIfPresent(itemToCopyFrom, a5, m);
		copyIfPresent(itemToCopyFrom, a6, m);
		copyIfPresent(itemToCopyFrom, a7, m);
		copyIfPresent(itemToCopyFrom, a8, m);
		copyIfPresent(itemToCopyFrom, a9, m);
		copyIfPresent(itemToCopyFrom, a10, m);
		return m;
	}

	private static void copyIfPresent(Item itemToCopyFrom, String attributeName, M<Object> m) {
		Object value = itemToCopyFrom.get(attributeName);
		if (value != null)
			m.put(attributeName, value);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static M<Object> map(String id, Object value) {
		M<Object> m = map();
		m.put(id, value);
		return m;
	}

	public static M<Object> map(String id1, Object value1, String id2, Object value2) {
		M<Object> m = map();
		m.put(id1, value1);
		m.put(id2, value2);
		return m;
	}

	public static M<Object> map(String id1, Object value1, String id2, Object value2, String id3, Object value3) {
		M<Object> m = map();
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		return m;
	}

	public static M<Object> map(String id1, Object value1, String id2, Object value2, String id3, Object value3, String id4, Object value4) {
		M<Object> m = map();
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		m.put(id4, value4);
		return m;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static Map<String, Object> m(String id, Object value) {
		Map<String, Object> m = new HashMap<>(5);
		m.put(id, value);
		return m;
	}

	public static Map<String, Object> m(String id1, Object value1, String id2, Object value2) {
		Map<String, Object> m = new HashMap<>(5);
		m.put(id1, value1);
		m.put(id2, value2);
		return m;
	}

	public static Map<String, Object> m(String id1, Object value1, String id2, Object value2, String id3, Object value3) {
		Map<String, Object> m = new HashMap<>(5);
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		return m;
	}

	public static Map<String, Object> m(String id1, Object value1, String id2, Object value2, String id3, Object value3, String id4,
	  Object value4) {
		Map<String, Object> m = new HashMap<>(6);
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		m.put(id4, value4);
		return m;
	}

	public static Map<String, Object> m(String id1, Object value1, String id2, Object value2, String id3, Object value3, String id4,
	  Object value4, String id5, Object value5) {
		Map<String, Object> m = new HashMap<>(8);
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		m.put(id4, value4);
		m.put(id5, value5);
		return m;
	}

	public static Map<String, Object> m(String id1, Object value1, String id2, Object value2, String id3, Object value3, String id4,
	  Object value4, String id5, Object value5, String id6, Object value6) {
		Map<String, Object> m = new HashMap<>(10);
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		m.put(id4, value4);
		m.put(id5, value5);
		m.put(id6, value6);
		return m;
	}

	public static Map<String, Object> m(String id1, Object value1, String id2, Object value2, String id3, Object value3, String id4,
	  Object value4, String id5, Object value5, String id6, Object value6, String id7, Object value7) {
		Map<String, Object> m = new HashMap<>(10);
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		m.put(id4, value4);
		m.put(id5, value5);
		m.put(id6, value6);
		m.put(id7, value7);
		return m;
	}

	public static Map<String, Object> m(String id1, Object value1, String id2, Object value2, String id3, Object value3, String id4,
	  Object value4, String id5, Object value5, String id6, Object value6, String id7, Object value7, String id8, Object value8) {
		Map<String, Object> m = new HashMap<>(12);
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		m.put(id4, value4);
		m.put(id5, value5);
		m.put(id6, value6);
		m.put(id7, value7);
		m.put(id8, value8);
		return m;
	}

	public static Map<String, Object> m(String id1, Object value1, String id2, Object value2, String id3, Object value3, String id4,
	  Object value4, String id5, Object value5, String id6, Object value6, String id7, Object value7, String id8, Object value8, String id9,
	  Object value9) {
		Map<String, Object> m = new HashMap<>();
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		m.put(id4, value4);
		m.put(id5, value5);
		m.put(id6, value6);
		m.put(id7, value7);
		m.put(id8, value8);
		m.put(id9, value9);
		return m;
	}

	public static Map<String, Object> m(String id1, Object value1, String id2, Object value2, String id3, Object value3, String id4,
	  Object value4, String id5, Object value5, String id6, Object value6, String id7, Object value7, String id8, Object value8, String id9,
	  Object value9, String id10, Object value10) {
		Map<String, Object> m = new HashMap<>();
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		m.put(id4, value4);
		m.put(id5, value5);
		m.put(id6, value6);
		m.put(id7, value7);
		m.put(id8, value8);
		m.put(id9, value9);
		m.put(id10, value10);
		return m;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static M<String> smap(String id, String value) {
		M<String> m = smap();
		m.put(id, value);
		return m;
	}

	public static M<String> smap(String id1, String value1, String id2, String value2) {
		M<String> m = smap();
		m.put(id1, value1);
		m.put(id2, value2);
		return m;
	}

	public static M<String> smap(String id1, String value1, String id2, String value2, String id3, String value3) {
		M<String> m = smap();
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		return m;
	}

	public static M<String> smap(String id1, String value1, String id2, String value2, String id3, String value3, String id4, String value4) {
		M<String> m = smap();
		m.put(id1, value1);
		m.put(id2, value2);
		m.put(id3, value3);
		m.put(id4, value4);
		return m;
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
