package com.cwworkshop;

import java.util.Arrays;
import java.util.List;

import software.amazon.awscdk.BundlingOptions;
import software.amazon.awscdk.DockerVolume;
import software.amazon.awscdk.services.lambda.Runtime;

import static java.util.Collections.singletonList;
import static software.amazon.awscdk.BundlingOutput.ARCHIVED;

public class LambdaBundling {
    public static BundlingOptions get(String packageName) {
        String cmd = "mvn clean install " +
                "&& cp /asset-input/target/%s.jar /asset-output/";

        List<String> packagingInstructions = Arrays.asList(
                "/bin/sh",
                "-c",
                String.format(cmd, packageName));

        BundlingOptions builderOptions = BundlingOptions.builder()
                .command(packagingInstructions)
                .image(Runtime.JAVA_11.getBundlingImage())
                .volumes(singletonList(
                        // Mount local .m2 repo to avoid download all the dependencies again inside the container
                        DockerVolume.builder()
                                .hostPath(System.getProperty("user.home") + "/.m2/")
                                .containerPath("/root/.m2/")
                                .build()))
                .user("root")
                .outputType(ARCHIVED)
                .build();

        return builderOptions;
    }
}
