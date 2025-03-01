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
    
    // <<Amazon CodeWhisperer generated code goes here>>

    // 3.) Publish item to SNS
    
    // <<Amazon CodeWhisperer generated code goes here>>

    // 4.) Delete message from SQS
    
    // <<Amazon CodeWhisperer generated code goes here>>
}
