package recognition;

import java.util.Map;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

public class LabelStorage {
    // 2.) Save json item to to DynamoDB
    public static void saveLabelsToDynamoDB(String tableName, Map<String, AttributeValue> dbItem) {
        // Create Client
        DynamoDbClient ddb = DynamoDbClient.create();
        // Create PutItemRequest object
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(dbItem)
                .build();
        try {
            // Put Item
            PutItemResponse response = ddb.putItem(request);
        } catch (Exception e) {
            System.err.println("Error saving item in DynamoDB: " + e);
        }
    }

    // 3.) Publish item to SNS
    public static void publishToSNS(String topicArn, String msg) {
        // Create Client
        SnsClient snsClient = SnsClient.create();
        // Create PublishRequest object
        PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(topicArn)
                .message(msg)
                .build();
        try {
            // Publish Message
            PublishResponse publishResponse = snsClient.publish(publishRequest);
        } catch (Exception e) {
            System.err.println("Error publishing message to SNS: " + e);
        }
    }

    // 4.) Delete message from SQS
    public static void deleteMessage(String queueUrl, String msgHandle) {
        // Create Client
        SqsClient sqsClient = SqsClient.create();
        // Create DeleteMessageRequest object
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(msgHandle)
                .build();
        try {
            // Delete Message
            sqsClient.deleteMessage(deleteMessageRequest);
        } catch (Exception e) {
            System.err.println("Error deleting message from SQS: " + e);
        }
    }
}
