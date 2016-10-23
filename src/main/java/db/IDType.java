package db;

public enum IDType {

	/** string value */
	STRING("S"),

	/** a number */
	NUMBER("N"),

	/** set of strings (unnordered - cannot contain duplicates) */
	STRING_SET("SS"),

	/** set of numbers (unnordered - cannot contain duplicates) */
	NUMBER_SET("NS"),

	/** list of strings (ordered - can contain duplicates) */
	LIST("l"),

	/** an item (a document) */
	ITEM("m"),

	/** list of items (documents) */
	ITEM_LIST("l");

	// ----------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------

	public final String typeKey;

	// ----------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param typeKey - chave do tipo - Ex: "S" p/ strings, na AWS
	 */
	IDType(String typeKey) {
		this.typeKey = typeKey;
	}

	// ----------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------------
}