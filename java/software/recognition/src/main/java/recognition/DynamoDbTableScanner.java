package recognition;

import java.util.List;
import java.util.stream.Collectors;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public final class DynamoDbTableScanner {

    // 1.) Function to list all items from a DynamoDB table
    public static List<List<String>> listItems(String tableName) {
        // Create a DynamoDbClient object
        DynamoDbClient ddb = DynamoDbClient.create();
        // Get all items from the table
        var response = ddb.scan(scanRequest -> scanRequest.tableName(tableName));
        var items = response.items().stream()
                .map(t -> t.values().stream().map(v -> v.s()).collect(Collectors.toList()))
                .collect(Collectors.toList());
        // Return all the items in the table
        return items;
    }
}
