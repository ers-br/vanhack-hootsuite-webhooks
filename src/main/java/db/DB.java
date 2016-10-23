package db;

import utils.E;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;

/**
 * Initializes DynamoDB local (a local hard-disk based database that has the same API and behavior as the AWS DynamoDB).
 */
public class DB {

	static AmazonDynamoDBClient dynamoClient;
	static DynamoDB dynamodb;

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static void init(int dynamoDBLocalPort, DBTable... tables) throws Exception {
		// DynamoDB config:
		ClientConfiguration config = new ClientConfiguration();
		config.setMaxErrorRetry(10);
		config.setMaxConnections(100);

		// create low-level API access client
		dynamoClient = new AmazonDynamoDBClient(config);
		dynamoClient.setEndpoint("http://localhost:" + dynamoDBLocalPort);
		dynamodb = new DynamoDB(dynamoClient);

		for (DBTable t : tables)
			t.init(dynamodb);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	public static void shutdown() throws Exception {
		if (dynamodb != null)
			dynamodb.shutdown();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static void createTable(String tableName, String hashKey, String rangeKey, IDType rangeKeyType) {
		try {
			DBCreate.createTable(tableName, hashKey, rangeKey, rangeKeyType, dynamoClient);
		}
		catch (Exception e) {
			if (e instanceof ResourceInUseException || e.getMessage() != null && e.getMessage().startsWith("Table already exists"))
				System.out.print("\nTable already exists: " + tableName);
			else {
				System.out.println("!!! Table [" + tableName + "] - Error while creating it..." + E.str(e));
				throw new RuntimeException(e);
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

}
