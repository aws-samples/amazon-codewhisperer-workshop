package integration;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Handler implements RequestHandler<SQSEvent, String> {

    public String handleRequest(SQSEvent event, Context context) {

        try {
            final var body = new ObjectMapper().writeValueAsString(event);

            // Call MailServerIntegration class with var "event" to convert json to xml
            String xml = MailServerIntegration.jsonToXml(body);

            // Call MailServerIntegration class to post xml
            String response = MailServerIntegration.sendPost(xml);
            
            return "200 OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }
}
