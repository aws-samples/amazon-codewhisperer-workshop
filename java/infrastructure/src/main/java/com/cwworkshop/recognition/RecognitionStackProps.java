package com.cwworkshop.recognition;

import org.jetbrains.annotations.Nullable;

import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class RecognitionStackProps implements StackProps {

    private String sqsUrl;
    private String sqsArn;
    private String snsArn;
    private Environment env;

    @Override
    public @Nullable Environment getEnv() {
        return this.env;
    }

    public String getSqsUrl() {
        return this.sqsUrl;
    }

    public String getSqsArn() {
        return this.sqsArn;
    }

    public String getSnsArn() {
        return this.snsArn;
    }

    public RecognitionStackProps sqsUrl(String sqsUrl) {
        this.sqsUrl = sqsUrl;
        return this;
    }

    public RecognitionStackProps sqsArn(String sqsArn) {
        this.sqsArn = sqsArn;
        return this;
    }

    public RecognitionStackProps snsArn(String snsArn) {
        this.snsArn = snsArn;
        return this;
    }

    public RecognitionStackProps withEnv(Environment env) {
        this.env = env;
        return this;
    }
}
