package db;

import java.util.ArrayList;
import java.util.List;

import utils.D;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

/**
 * Creates tables on Amazon DynamoDB.
 */
class DBCreate {

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	static void createTable(String tableName, String hashKey, String rangeKey, IDType rangeKeyType, AmazonDynamoDBClient dynamoClient) {
		final List<AttributeDefinition> defs = new ArrayList<>();
		final List<KeySchemaElement> schemas = new ArrayList<>();
		defs.add(new AttributeDefinition().withAttributeName(hashKey).withAttributeType(IDType.STRING.typeKey));

		// criar a request com o nome da tabela, o esquema com o atributo 'hash' da chave primária e tb com o throughput
		schemas.add(new KeySchemaElement().withAttributeName(hashKey).withKeyType(KeyType.HASH));
		if (rangeKey != null) {
			schemas.add(new KeySchemaElement().withAttributeName(rangeKey).withKeyType(KeyType.RANGE));
			defs.add(new AttributeDefinition().withAttributeName(rangeKey).withAttributeType(rangeKeyType.typeKey));
		}

		ProvisionedThroughput throughput = new ProvisionedThroughput();
		throughput.withReadCapacityUnits(1L).withWriteCapacityUnits(1L);
		CreateTableRequest request = new CreateTableRequest(tableName, schemas).withProvisionedThroughput(throughput);

		dynamoClient.createTable(request.withAttributeDefinitions(defs));
		waitTableCreation(tableName, dynamoClient);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------

	private static void waitTableCreation(final String tableName, AmazonDynamoDBClient dynamoClient) {
		System.out.println("DynamoDB - tabela sendo criada: " + tableName);
		long exp = System.currentTimeMillis() + 2 * D.MINUTE_MILLIS;
		DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
		while (System.currentTimeMillis() < exp) {
			try {
				DescribeTableResult info = dynamoClient.describeTable(describeTableRequest);
				if (info.getTable().getTableStatus().equals("ACTIVE")) {
					System.out.println("DynamoDB - tabela ativa: " + tableName);
					return;
				}
				System.out.println("DynamoDB - tabela em '" + info.getTable().getTableStatus() + "': " + tableName);
			}
			catch (ResourceNotFoundException e) {}
			try {
				Thread.sleep(5000);
			}
			catch (InterruptedException e) {}
		}
		System.out.println("DynamoDB - WARN creating " + tableName + " - TEMPO EXPIRADO");
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
