# vanhack-hootsuite-webhooks

Project created during a hackathon (~27 hrs), to solve a challenge from HootSuite, with the goal to make a highly scalable messaging 
system (webhooks) where delivery order is a must have.

This implementation uses only AWS DynamoDB for the sake of simplicity, but it can scale really well.

DynamoDB's model divides the data into partitions, each one retrieved by a hashKey.
Inside each partition, there is an always ordered table where it's primary key is called the 'range key' or 'sort key'.

DynamoDB is configurable by 'reads/writes per second' and it can scale infinitely by number of partitions (in our case, destinations).
Inside each partition, it can scale up to 10gb. So considering this messaging system, there is no limit for the number of destinations 
and there is a 10gb limit per destination. Acting like a queue, you can have up to 10gb of 'messages to be sent' to the same destination. Every time a message is successfully sent, it is deleted from the table. So the 10gb limit applies only to new messages.

====================================================================================================

To run the service, run class 'HootApp'. It has no state, relying only on the DB. This way you can launch several of them behind a 
load-balancer to achieve desired scale. It is recommended due to the chosen architecture, where only one thread is processing all 
destinations. 

====================================================================================================

To run everything locally, run DynamoDB-Local in port 8010 (or set the port like below)
 
 --> http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html
  
  (I will embed it into this project but I should not change code right now, due to hackathon)

====================================================================================================

**Eclipse**

If you are using eclipse, check the launcher menu for HootApp (created based on file HootApp.launch)

====================================================================================================

**Other IDEs**

java {opts} HootApp [listen-port] [dynamoDB-local-port]

--> defaults to 'HootApp 80 8010'

opts:
-Xmx500M
-Dfile.encoding=UTF-8
-Daws.accessKeyId=AAAAAAAAAAAA
-Daws.secretKey=SEEEEEEECCRRRRREEEETTT"

Note that (weirdly) to connect to DynamoDB-Local you should supply some aws keys. But any will work.

====================================================================================================

To run it in real DynamoDB, comment the line setEndpoint(...) in class DB, and set your real aws keys in the starting params.
(I cannot change classes right now due to hackathon rules)

Line 30 (db.DB.java):  dynamoClient.setEndpoint("http://localhost:" + dynamoDBLocalPort);

====================================================================================================

**Testing**

Check out the classes inside 'test' package. Most of them perform a little test calling REST services of HootApp.

These 2 are noteworthy:

**WebhookEndpoint**

This app creates 3 endpoints (just change the code to create more) that acts as message destinations.
Each endpoint registers itself as a destination, listening on a port starting from 10000.

**WebhookSender**

You can then run WebhookSender, which sends random messages to all destinations. 



Thank you for your time.
