package recognition;

import java.util.HashMap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class RecognitionHandler implements RequestHandler<SQSEvent, String> {

    private static final String queueUrl = System.getenv("SQS_QUEUE_URL");
    private static final String tableName = System.getenv("TABLE_NAME");
    private static final String topicArn = System.getenv("TOPIC_ARN");

    public String handleRequest(SQSEvent event, Context context) {
        try {
            for (var eventRecord : event.getRecords()) {
                final var receiptHandle = eventRecord.getReceiptHandle();
                
                final ObjectMapper mapper = new ObjectMapper();
                final JsonNode root = mapper.readTree(eventRecord.getBody());

                for (final var record : root.get("Records") ) {
                    final var bucketName = record.get("s3").get("bucket").get("name").asText();
                    final var key = record.get("s3").get("object").get("key").asText();

                    // call the ImageRecognizer method #1 to generate image label and store as var "labels"
                    
                    // <<Amazon CodeWhisperer generated code goes here>>

                    // code snippet to create dynamodb item from labels
                    final var labelsString = new ObjectMapper().writeValueAsString(labels);
                    System.out.println("Detected labels: " + labelsString);
                    
                    final var dbItem = new HashMap<String, AttributeValue>();
                    dbItem.put("image", AttributeValue.fromS(key));
                    dbItem.put("labels", AttributeValue.fromS(labelsString));

                    // call the LabelStorage method #1 to store "dbItem" result on DynamoDB
                    
                    // <<Amazon CodeWhisperer generated code goes here>>
            
                    // call the LabelStorage method method #2 to send message to SNS
                    
                    // <<Amazon CodeWhisperer generated code goes here>>

                    // call the LabelStorage method method #3 to delete img from SQS
                    
                    // <<Amazon CodeWhisperer generated code goes here>>
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while recognizing images");
            e.printStackTrace();
            return "500 Internal Server Error";
        }

        return "200 OK";
    }
}
