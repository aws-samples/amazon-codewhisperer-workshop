package recognition;

import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;

public class ImageRecognizer {

    // 1.) Function to detect labels from image with Rekognition as "labels"
    public static String[] detectLabels(String bucketName, String imageName) {
        String[] labels = null;
        try {
            // Initialize Rekognition client
            RekognitionClient rekClient = RekognitionClient.create();

            // Set the image to be searched for faces
            DetectLabelsRequest request = DetectLabelsRequest.builder()
                    .image(Image.builder()
                            .s3Object(builder -> builder.bucket(bucketName).name(imageName))
                            .build())
                    .minConfidence(70.0f)
                    .maxLabels(10)
                    .build();

            // Call Rekognition to detect the labels
            DetectLabelsResponse response = rekClient.detectLabels(request);
            labels = response.labels().stream().map(label -> label.name()).toArray(String[]::new);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return labels;
    }
}
