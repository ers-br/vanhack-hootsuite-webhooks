package db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import utils.A;
import utils.B;
import utils.D;
import utils.I;
import utils.N;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryFilter;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.RangeKeyCondition;
import com.amazonaws.services.dynamodbv2.document.ScanFilter;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

/**
 * Tabelas básicas para um sistema usando DynamoDB (AWS).
 * 
 * <pre>
 * 
 * DynamoDB - Cada item pode ter no máximo 400KB.
 * 
 * Local Secondary Index:
 *  - In practice: you have up to 5 more range indexes - queries always over the same hash-key.
 * 
 * Global Secondary Index:
 *  - Indexes (in a virtual table) several items - multiple hash+range pairs.
 * 
 * </pre>
 */
public class DBTable {

	public final String name;
	public final String hashKey;
	public final String rangeKey;

	public final boolean cTOnInsert;

	private final Expected expectedHashDoesNotExist;
	private final Expected expectedRangeDoesNotExist;

	/** AWS SDK - DynamoDB table gateway */
	private Table table;

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public DBTable(String name, String hash, String range, boolean cTOnInsert) {
		this.name = name;
		this.hashKey = hash;
		this.rangeKey = range;
		this.expectedHashDoesNotExist = new Expected(hashKey).notExist();
		this.expectedRangeDoesNotExist = range == null ? null : new Expected(rangeKey).notExist();
		this.cTOnInsert = cTOnInsert;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/** Chamado depois de inicializarmos a AWS. */
	public void init(DynamoDB dynamodb) {
		this.table = dynamodb.getTable(name);
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	// INSERT

	public PutItemOutcome insertItem(Item newItem) {
		if (cTOnInsert)
			newItem.withLong(I.TIMESTAMP_CREATED_AT, System.currentTimeMillis());
		return insert(new PutItemSpec().withItem(newItem));
	}

	/** Adds expected to 'not-exist' on both the hash and range values. */
	public PutItemOutcome insertNewItem(Item newItem) {
		if (cTOnInsert)
			newItem.withLong(I.TIMESTAMP_CREATED_AT, System.currentTimeMillis());
		if (rangeKey == null)
			return insert(new PutItemSpec().withItem(newItem).withExpected(expectedHashDoesNotExist));
		return insert(new PutItemSpec().withItem(newItem).withExpected(expectedHashDoesNotExist, expectedRangeDoesNotExist));
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * First this method fills the 'seq' field (String) with the current date (readableTime + milliseconds). Then it tries to insert the item
	 * as a unique [hash + seq] combination. It adds 'expected' (<b>not-exist</b>) on both the hash and seq values, and tries to insert the
	 * item. If a conflict occurs on the first attempt, at most 'maxRetries' are attempted next, with THE SAME 'base seq' on every iteration
	 * of the process, but with some data appended to it after a space (only ConditionalCheckFailedException is caught, assuming another
	 * thread created the item already).
	 * 
	 * @param newItem
	 * @param maxRetries - If a conflict occurs on the first attempt, at most 'maxRetries' are attempted, with the same 'seq' but a new
	 *        appended suffix after a space.
	 * @return outcome.
	 */
	public PutItemOutcome insertNewItemRetrySEQ(Item newItem, int maxRetries) {
		if (!I.SEQUENCE.equals(rangeKey))
			throw new RuntimeException("insertNewItemRetrySEQ - Range must be 'seq' - Range for table '" + name + "': " + rangeKey);

		maxRetries++; // always try at least once
		final long currentTime = System.currentTimeMillis();

		if (cTOnInsert)
			newItem.withLong(I.TIMESTAMP_CREATED_AT, currentTime);

		final String readableTimestamp_BASE = D.readableTimestamp(B.buff(), D.cal(currentTime)).toString();
		String readableTimestamp = readableTimestamp_BASE;

		PutItemSpec spec = new PutItemSpec().withItem(newItem).withExpected(expectedHashDoesNotExist, expectedRangeDoesNotExist);
		int i = 0;
		for (; i < maxRetries; i++) {
			try {
				newItem.withString(I.SEQUENCE, readableTimestamp);
				return insert(spec); // most cases get here and return in the first attempt
			}
			catch (ConditionalCheckFailedException e) {
				// oops, some other thread created this message! Since the message got here at the time already in _BASE, let's keep it and
				// just include a suffix, which won't break the ordering (won't change the timestamp)
				System.out.println(B.str("Table [", name, "] CONFLICT inserting item (retry SEQ) [attempt: ", N.str(i + 1), "]\nItem: ",
				  newItem.toJSONPretty()));
				String suffix = " " + StringUtils.leftPad(Integer.toString(i), Integer.toString(maxRetries).length(), "0");
				readableTimestamp = readableTimestamp_BASE + suffix;
			}
		}

		throw new RuntimeException("Table [" + name + "] COULD not create unique item: " + newItem.toJSONPretty());
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public PutItemOutcome insertItem(Item newItem, Expected... expected) {
		if (cTOnInsert)
			newItem.withLong(I.TIMESTAMP_CREATED_AT, System.currentTimeMillis());
		return insert(new PutItemSpec().withItem(newItem).withExpected(expected));
	}

	public PutItemOutcome insertItem_NoCreationTimestamp(Item newItem, Expected... expected) {
		return insert(new PutItemSpec().withItem(newItem).withExpected(expected));
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// GET

	/**
	 * @param hashValue - the key.
	 * @return item from DB (the cache - if present - is ignored, and not updated). Returns NULL if not found on DB.
	 */
	public Item getItem(String hashValue) {
		Item item = table.getItem(hashKey, hashValue);
		return item;
	}

	/**
	 * @param hashValue - the hash key.
	 * @param rangeValue - the range key.
	 * @return item from DB (the cache - if present - is ignored, and not updated). Returns NULL if not found on DB.
	 */
	public Item getItem(String hashValue, Object rangeValue) {
		Item item = table.getItem(hashKey, hashValue, rangeKey, rangeValue);
		return item;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public DeleteItemOutcome deleteItem(String hashValue) {
		DeleteItemOutcome outcome = delete(new DeleteItemSpec().withPrimaryKey(hashKey, hashValue));
		return outcome;
	}

	public DeleteItemOutcome deleteItem(String hashValue, Object rangeValue) {
		DeleteItemOutcome outcome = delete(new DeleteItemSpec().withPrimaryKey(hashKey, hashValue, rangeKey, rangeValue));
		return outcome;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public Item update(PrimaryKey key, AttributeUpdate... updates) {
		UpdateItemSpec spec = new UpdateItemSpec().withPrimaryKey(key).withAttributeUpdate(updates);
		return update(spec);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// UPDATE

	/** @return The updated item on success, or NULL if the expected condition was not met. For other errors the exception is thrown up. */
	public Item update(PrimaryKey key, Expected exp, AttributeUpdate... updates) {
		UpdateItemSpec spec = new UpdateItemSpec().withPrimaryKey(key).withExpected(exp).withAttributeUpdate(updates);
		try {
			return update(spec);
		}
		catch (ConditionalCheckFailedException e) {
			throw new RuntimeException("Expected Condition Failed!\nTable: " + name + "\nKeys: " + key.toString() + "\nError: " + e.getMessage());
		}
	}

	public Item update(String hashValue, AttributeUpdate... updates) {
		PrimaryKey pk = new PrimaryKey(hashKey, hashValue);
		return update(pk, updates);
	}

	/** @return The updated item on success, or NULL if the expected condition was not met. For other errors the exception is thrown up. */
	public Item update(String hashValue, Expected exp, AttributeUpdate... updates) {
		PrimaryKey pk = new PrimaryKey(hashKey, hashValue);
		return update(pk, exp, updates);
	}

	public Item update(String hashValue, Object rangeValue, AttributeUpdate... updates) {
		PrimaryKey pk = new PrimaryKey(hashKey, hashValue, rangeKey, rangeValue);
		return update(pk, updates);
	}

	/** @return The updated item on success, or NULL if the expected condition was not met. For other errors the exception is thrown up. */
	public Item update(String hashValue, Object rangeValue, Expected exp, AttributeUpdate... updates) {
		PrimaryKey pk = new PrimaryKey(hashKey, hashValue, rangeKey, rangeValue);
		return update(pk, exp, updates);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// update AND get updated: hash + range

	public Item update(String hashValue, Object rangeVal, String attr, Object newValue) {
		PrimaryKey pk = new PrimaryKey(hashKey, hashValue, rangeKey, rangeVal);
		AttributeUpdate update = A.up(attr, newValue);
		return update(pk, update);
	}

	public Item update(String hashValue, Object rangeVal, String attr1, Object val1, String attr2, Object val2) {
		PrimaryKey pk = new PrimaryKey(hashKey, hashValue, rangeKey, rangeVal);
		AttributeUpdate up1 = A.up(attr1, val1);
		AttributeUpdate up2 = A.up(attr2, val2);
		return update(pk, up1, up2);
	}

	public Item update(String hashValue, Object rangeVal, String attr1, Object val1, String attr2, Object val2, String attr3, Object val3) {
		PrimaryKey pk = new PrimaryKey(hashKey, hashValue, rangeKey, rangeVal);
		AttributeUpdate up1 = A.up(attr1, val1);
		AttributeUpdate up2 = A.up(attr2, val2);
		AttributeUpdate up3 = A.up(attr3, val3);
		return update(pk, up1, up2, up3);
	}

	public Item update(String hashValue, Object rangeVal, String attr1, Object val1, String attr2, Object val2, String attr3, Object val3,
	  String attr4, Object val4) {
		PrimaryKey pk = new PrimaryKey(hashKey, hashValue, rangeKey, rangeVal);
		AttributeUpdate up1 = A.up(attr1, val1);
		AttributeUpdate up2 = A.up(attr2, val2);
		AttributeUpdate up3 = A.up(attr3, val3);
		AttributeUpdate up4 = A.up(attr4, val4);
		return update(pk, up1, up2, up3, up4);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public ItemCollection<QueryOutcome> query(String hashValue, boolean scanForward, String... attribsToGet) {
		return _query(hashValue, null, scanForward, null, -1, -1, attribsToGet);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public Item queryLastItem(String hashValue, String... attribsToGet) {
		ItemCollection<QueryOutcome> results = _query(hashValue, null, false, null, -1, 1, attribsToGet);
		for (Item item : results)
			return item;
		return null;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public ItemCollection<QueryOutcome> _query(String hashValue, RangeKeyCondition range, boolean scanForward, QueryFilter[] filters,
	  int maxPageSize, int maxResults, String... attribsToGet) {

		// create query config
		QuerySpec querySpec = new QuerySpec().withHashKey(hashKey, hashValue).withScanIndexForward(scanForward);

		// max page size and max results for all pages
		if (maxPageSize > 0)
			querySpec.withMaxPageSize(maxPageSize);
		if (maxResults > 0)
			querySpec.withMaxResultSize(maxResults);

		// range condition
		if (range != null)
			querySpec.withRangeKeyCondition(range);

		// attributes to get
		if (attribsToGet.length > 0)
			querySpec.withAttributesToGet(attribsToGet);

		// filters for the query
		if (filters != null && filters.length > 0)
			querySpec.withQueryFilters(filters);

		// do it!
		long startTime = System.currentTimeMillis();
		ItemCollection<QueryOutcome> query = table.query(querySpec.withReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL));
		ConsumedCapacity consumed = query.getTotalConsumedCapacity();
		String capacityUsed = consumed != null ? " [Consumed: " + consumed.toString() + "]" : " <no 'consumed' info>";
		System.out.println(B.str("Query in ", name, ": ", D.diffTimeStr(startTime), capacityUsed));
		return query;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public List<Item> query(String hashValue, RangeKeyCondition range, boolean scanForward, String... attribsToGet) {
		ItemCollection<QueryOutcome> query = _query(hashValue, range, scanForward, null, -1, -1, attribsToGet);
		List<Item> list = new ArrayList<>(100);
		for (Item item : query)
			list.add(item);
		return list;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public List<Item> scan(ScanFilter... filters) {
		List<Item> list = new ArrayList<>(1000);
		ItemCollection<ScanOutcome> scan = table.scan(filters);
		for (Item item : scan)
			list.add(item);
		return list;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public List<Item> getAll() {
		List<Item> list = new ArrayList<>(1000);
		ItemCollection<ScanOutcome> scan = table.scan(new ScanSpec());
		for (Item item : scan)
			list.add(item);
		return list;
	}

	public ItemCollection<ScanOutcome> queryAll() {
		return table.scan(new ScanSpec());
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public String idFromHashRange(Item item) {
		String partitionKey = (String) item.get(hashKey);
		if (rangeKey == null)
			return partitionKey;
		return B.str(partitionKey, "_", item.getString(rangeKey));
	}

	public String idFromHashRange(Map<String, Object> item) {
		String partitionKey = (String) item.get(hashKey);
		if (rangeKey == null)
			return partitionKey;
		return B.str(partitionKey, "_", item.get(rangeKey).toString());
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public ItemCollection<QueryOutcome> query(QuerySpec spec) {
		return table.query(spec);
	}

	public ItemCollection<ScanOutcome> scan(ScanSpec spec) {
		return table.scan(spec);
	}

	public List<Map<String, Object>> queryEx(String hashValue, boolean scanForward, String... attribsToGet) {
		QuerySpec spec = createQuerySpec(hashValue).withScanIndexForward(scanForward);
		if (attribsToGet.length > 0)
			spec.withAttributesToGet(attribsToGet);
		ItemCollection<QueryOutcome> query = table.query(spec);
		List<Map<String, Object>> list = new ArrayList<>();
		for (Item item : query)
			list.add(item.asMap());
		return list;
	}

	public List<Map<String, Object>> queryEx(QuerySpec spec) {
		ItemCollection<QueryOutcome> query = table.query(spec);
		List<Map<String, Object>> list = new ArrayList<>();
		for (Item item : query)
			list.add(item.asMap());
		return list;
	}

	public List<Map<String, Object>> scanEx(ScanSpec spec) {
		ItemCollection<ScanOutcome> query = table.scan(spec);
		List<Map<String, Object>> list = new ArrayList<>();
		for (Item item : query)
			list.add(item.asMap());
		return list;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public PrimaryKey pk(Object hashValue) {
		return new PrimaryKey(hashKey, hashValue);
	}

	public PrimaryKey pk(Object hashValue, Object rangeValue) {
		return new PrimaryKey(hashKey, hashValue, rangeKey, rangeValue);
	}

	public UpdateItemSpec createUpdateSpec(String hashValue) {
		return new UpdateItemSpec().withPrimaryKey(hashKey, hashValue);
	}

	public UpdateItemSpec createUpdateSpec(String hashValue, Object rangeValue) {
		return new UpdateItemSpec().withPrimaryKey(hashKey, hashValue, rangeKey, rangeValue);
	}

	public QuerySpec createQuerySpec(String hashValue) {
		return new QuerySpec().withHashKey(hashKey, hashValue);
	}

	public QuerySpec createQuerySpec(String hashValue, RangeKeyCondition rangeKeyCondition) {
		return new QuerySpec().withHashKey(hashKey, hashValue).withRangeKeyCondition(rangeKeyCondition);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public Item getFirstItem(String hash) {
		if (rangeKey == null)
			throw new RuntimeException("Table has no range to get the `first item`: " + name + "\n - hashValue: " + hash);
		QuerySpec querySpec = new QuerySpec().withHashKey(hashKey, hash).withScanIndexForward(true).withMaxResultSize(1);
		ItemCollection<QueryOutcome> outcome = table.query(querySpec);
		Item first = null;
		for (Item item : outcome)
			first = item;
		return first;
	}

	public Item getLastItem(String hash) {
		if (rangeKey == null)
			throw new RuntimeException("Table has no range to get the `last item`: " + name + "\n - hashValue: " + hash);
		QuerySpec querySpec = new QuerySpec().withHashKey(hashKey, hash).withScanIndexForward(false).withMaxResultSize(1);
		ItemCollection<QueryOutcome> outcome = table.query(querySpec);
		Item last = null;
		for (Item item : outcome)
			last = item;
		return last;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public List<Item> getFirstItems(String hash, int maxResults) {
		if (rangeKey == null)
			throw new RuntimeException("Table has no range to get the `first items`: " + name + "\n - hashValue: " + hash);
		QuerySpec querySpec = new QuerySpec().withHashKey(hashKey, hash).withScanIndexForward(true).withMaxResultSize(maxResults);
		ItemCollection<QueryOutcome> outcome = table.query(querySpec);
		List<Item> list = new ArrayList<>(maxResults);
		for (Item item : outcome)
			list.add(item);
		return list;
	}

	public List<Item> getLastItems(String hash, int maxResults) {
		if (rangeKey == null)
			throw new RuntimeException("Table has no range to get the `last items`: " + name + "\n - hashValue: " + hash);
		QuerySpec querySpec = new QuerySpec().withHashKey(hashKey, hash).withScanIndexForward(false).withMaxResultSize(maxResults);
		ItemCollection<QueryOutcome> outcome = table.query(querySpec);
		List<Item> list = new ArrayList<>(maxResults);
		for (Item item : outcome)
			list.add(item);
		return list;
	}

	public List<Map<String, Object>> getFirstItemsEx(String hash, int maxResults) {
		if (rangeKey == null)
			throw new RuntimeException("Table has no range to get the `first items`: " + name + "\n - hashValue: " + hash);
		QuerySpec querySpec = new QuerySpec().withHashKey(hashKey, hash).withScanIndexForward(true).withMaxResultSize(maxResults);
		ItemCollection<QueryOutcome> outcome = table.query(querySpec);
		List<Map<String, Object>> list = new ArrayList<>(maxResults);
		for (Item item : outcome)
			list.add(item.asMap());
		return list;
	}

	public List<Map<String, Object>> getLastItemsEx(String hash, int maxResults) {
		if (rangeKey == null)
			throw new RuntimeException("Table has no range to get the `last items`: " + name + "\n - hashValue: " + hash);
		QuerySpec querySpec = new QuerySpec().withHashKey(hashKey, hash).withScanIndexForward(false).withMaxResultSize(maxResults);
		ItemCollection<QueryOutcome> outcome = table.query(querySpec);
		List<Map<String, Object>> list = new ArrayList<>(maxResults);
		for (Item item : outcome)
			list.add(item.asMap());
		return list;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	Map<String, Object> mapFromPK(Collection<KeyAttribute> components) {
		Map<String, Object> map = new HashMap<>();
		for (KeyAttribute k : components) {
			String key = k.getName();
			Object val = k.getValue();
			map.put(key, val);
		}
		return map;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public String toString() {
		return name;
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
	// BASE CRUD METHODs for the TABLE ... all others must call one of these so the updates are created accordingly.

	public PutItemOutcome insert(PutItemSpec spec) {
		PutItemOutcome outcome = table.putItem(spec);
		return outcome;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * UPDATE returning the new item.
	 * 
	 * @param spec
	 * @return the updated item.
	 */
	public Item update(UpdateItemSpec spec) {
		UpdateItemOutcome outcome = table.updateItem(spec.withReturnValues(ReturnValue.ALL_NEW));
		Item item = outcome.getItem();
		return item;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// DELETE an item

	public DeleteItemOutcome delete(DeleteItemSpec spec) {
		DeleteItemOutcome outcome = table.deleteItem(spec.withReturnValues(ReturnValue.ALL_OLD));
		return outcome;
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
