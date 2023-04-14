package recognition;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ListItemsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
    private static final String tableName = System.getenv("TABLE_NAME");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        
        // Call the DynamoDbTableScanner method to get all items from the table into the "items" var
        
        // <<Amazon CodeWhisperer generated code goes here>>
        
        try {
            final String body = new ObjectMapper().writeValueAsString(items);
            return new APIGatewayProxyResponseEvent().withBody(body);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody(e.getMessage());
        }
    }
}
