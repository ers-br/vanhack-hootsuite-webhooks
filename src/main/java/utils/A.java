package utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.util.json.Jackson;

/**
 * Amazon java sdk utilities.
 */
public final class A {

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static AttributeUpdate up(String info, Object value) {
		return new AttributeUpdate(info).put(value);
	}

	public static AttributeUpdate put(String info, Object value) {
		return new AttributeUpdate(info).put(value);
	}

	public static AttributeUpdate add(String info, Object... valuesToAdd) {
		return new AttributeUpdate(info).addElements(valuesToAdd);
	}

	public static AttributeUpdate add(String info, Number n) {
		return new AttributeUpdate(info).addNumeric(n);
	}

	public static AttributeUpdate sub(String info, Integer n) {
		return new AttributeUpdate(info).addNumeric(-n);
	}

	public static AttributeUpdate sub(String info, BigDecimal n) {
		return new AttributeUpdate(info).addNumeric(N.ZERO.subtract(n));
	}

	public static AttributeUpdate del(String info, Object... valuesToRemove) {
		AttributeUpdate del = new AttributeUpdate(info);
		return valuesToRemove.length == 0 ? del.delete() : del.removeElements(valuesToRemove);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static Item item(String id, String value) {
		return new Item().withString(id, value);
	}

	public static Item item(String id1, String value1, String id2, String value2) {
		return new Item().withString(id1, value1).withString(id2, value2);
	}

	public static Item item(String id1, String value1, String id2, String value2, String id3, String value3) {
		return new Item().withString(id1, value1).withString(id2, value2).withString(id3, value3);
	}

	public static Item item(String id1, String value1, String id2, String value2, String id3, String value3, String id4, String value4) {
		return new Item().withString(id1, value1).withString(id2, value2).withString(id3, value3).withString(id4, value4);
	}

	public static Item item(String id, Object value) {
		return new Item().with(id, value);
	}

	public static Item item(String id1, Object value1, String id2, Object value2) {
		return new Item().with(id1, value1).with(id2, value2);
	}

	public static Item item(String id1, Object value1, String id2, Object value2, String id3, Object value3) {
		return new Item().with(id1, value1).with(id2, value2).with(id3, value3);
	}

	public static Item item(String id1, Object value1, String id2, Object value2, String id3, Object value3, String id4, Object value4) {
		return new Item().with(id1, value1).with(id2, value2).with(id3, value3).with(id4, value4);
	}

	public static Item item(String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5) {
		return new Item().with(a1, v1).with(a2, v2).with(a3, v3).with(a4, v4).with(a5, v5);
	}

	public static Item item(String a1, Object v1, String a2, Object v2, String a3, Object v3, String a4, Object v4, String a5, Object v5,
	  String a6, Object v6) {
		return new Item().with(a1, v1).with(a2, v2).with(a3, v3).with(a4, v4).with(a5, v5).with(a6, v6);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Copy or erase (set to null) specified attributes into the 'to' item, according to its value in the 'from' item.
	 * 
	 * @param from - source item - will not be modified, but its values will be shallow-copied to 'to'.
	 * @param to - item to be modified.
	 * @param ids - what attributes to get from 'from' and copy/erase into 'to'.
	 */
	public static void copyOrDelete(Item from, Item to, String... ids) {
		for (String id : ids)
			if (from.hasAttribute(id))
				to.with(id, from.get(id));
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Copy specified attributes to a new item, but only if the attrib value is not null and also not empty.
	 * 
	 * @return the new item with the copied mappings (shallow-copy).
	 */
	public static Item copyNonEmpty(Item original, String... keys) {
		Item copy = new Item();
		for (String key : keys) {
			Object val = original.get(key);
			if (val == null)
				continue;

			if (val instanceof String) {
				if (((String) val).isEmpty())
					continue;
			}
			else if (val instanceof Collection) {
				if (((Collection<?>) val).isEmpty())
					continue;
			}
			else if (val instanceof Map && ((Map<?, ?>) val).isEmpty())
				continue;

			// ok, the value seems ok!
			copy.with(key, val);
		}
		return copy;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Copy specified attributes to the destination item, but only if the attrib value is not null and also not empty.
	 */
	public static void copyNonEmpty(Item original, Item destination, String... keys) {
		for (String key : keys) {
			Object val = original.get(key);
			if (val == null)
				continue;

			if (val instanceof String) {
				if (((String) val).isEmpty())
					continue;
			}
			else if (val instanceof Collection) {
				if (((Collection<?>) val).isEmpty())
					continue;
			}
			else if (val instanceof Map && ((Map<?, ?>) val).isEmpty())
				continue;

			// ok, the value seems ok!
			destination.with(key, val);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param itemList - list of items.
	 * @return ArrayList of Maps.
	 */
	public static List<Map<String, Object>> array(Iterable<Item> itemList) {
		List<Map<String, Object>> arr = new ArrayList<>();
		for (Item item : itemList)
			arr.add(item.asMap());
		return arr;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a JSON-Array-String with the provided elements.
	 * 
	 * @param itemList - list of DB Items, JSON formatted Strings.
	 * @return a String (JSONArray) containing all the elements.
	 */
	public static String jsonArrayString(Iterable<Item> itemList) {
		StringBuilder buff = B.buff().append('[');
		for (Item item : itemList)
			buff.append('\n').append(item.toJSONPretty()).append(',');
		int length = buff.length();
		if (length == 1)
			return "[]"; // nenhum item em 'itemList'
		buff.setCharAt(length - 1, '\n'); // trocar a última vírgula por '\n'
		return buff.append(']').toString();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a JSON-Array-String with the provided raw JSON-string elements.
	 * 
	 * @param jsonList - list of JSON formatted Strings.
	 * @return a JSON Array (serialized) containing all the elements.
	 */
	public static String jsonArrayStringRaw(List<String> jsonList) {
		StringBuilder buff = B.buff().append('[');
		for (String item : jsonList)
			buff.append('\n').append(item).append(',');
		int length = buff.length();
		if (length == 1)
			return "[]"; // nenhum item em 'itemList'
		buff.setCharAt(length - 1, '\n'); // trocar a última vírgula por '\n'
		return buff.append(']').toString();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String idFromHashRange(String hash, String range) {
		return B.str(hash, "_", range);
	}

	public static String idFromHashRange(String hash, long range) {
		return B.str(hash, "_", Long.toString(range));
	}

	public static String idFromHashRange(String hash, Object range) {
		return B.str(hash, "_", range.toString());
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static String str(Object data) {
		return Jackson.toJsonPrettyString(data);
	}

	public static String _str(Object data) {
		return Jackson.toJsonString(data);
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
