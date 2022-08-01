package api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final String S3_BUCKET = System.getenv("BUCKET_NAME");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

        final String url = event.getQueryStringParameters().get("url");
        final String name = "/tmp/" + event.getQueryStringParameters().get("name");

        // pass the output of method #1 as input to method #2
        FileDownloader.downloadFile(url, name);
        FileDownloader.uploadFile(name, S3_BUCKET);

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Successfully Uploaded Img!");
    }
}
