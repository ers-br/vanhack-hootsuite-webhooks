# vanhack-hootsuite-webhooks
Project created during a hackathon (~27 hrs), to solve a challenge from HootSuite, with the goal to make a highly scalable messaging 
system (webhooks) where delivery order is a must have.

This implementation uses only AWS DynamoDB for the sake of simplicity, but it can scale really well.

DynamoDB's model divides the data into partitions, each one retrieved by a hashKey.
Inside each partition, there is an always ordered table where it's primary key is called the 'rangeKey' or 'sort key'.

DynamoDB is configurable by 'reads/writes per second' and it can scale infinitely by number of partitions (in our case, destinations).
Inside each partition, it can scale up to 10gb. So considering this messaging system, there is no limit for the number of destinations 
and there is a 10gb limit per destination. Acting like a queue, you can have up to 10gb of 'messages to be sent' to the same destination. 
Every time a message is successfully sent, it is deleted from the table. So the 10gb limit applies only to new messages.

====================================================================================================

To run the service, run class 'HootApp'. It has no state, relying only on the DB. This way you can launch several of them behind a 
load-balancer to achieve desired scale. It is recommended due to the chosen architecture, where only one thread is processing all 
destinations. 

====================================================================================================

If you are using eclipse, check the launcher menu for HootApp (created based on file HootApp.launch)

====================================================================================================

If you are not using eclipse:

HootApp <opts> [listen-port] [dynamoDB-local-port]

--> defaults to 'HootApp <opts> 80 8010'

opts:
-Xmx500M
-Dfile.encoding=UTF-8
-Daws.accessKeyId=AAAAAAAAAAAA&
-Daws.secretKey=SEEEEEEECCRRRRREEEETTT"

====================================================================================================

To run everything locally, run DynamoDB-Local in port 8010 (or set the port)
  --> http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html

====================================================================================================

To run it in real DynamoDB, comment the line setEndpoint(...) in class DB, and set your real aws keys in the starting params.
(I cannot change classes right now due to hackathon rules)

Line 30 (db.DB.java):  dynamoClient.setEndpoint("http://localhost:" + dynamoDBLocalPort);

====================================================================================================

Testing inside eclipse: Check out the classes inside 'test' package. Each one performs a little test, 
calling REST services of HootApp.

WebhookEndpoint: this one creates 3 endpoints (just change the code to create more).
Each endpoint registers itselt as a destination, listening on a port starting from 10000.

You can then run WebhookSender, which sends random messages to all destinations.

Thank you for your time.
