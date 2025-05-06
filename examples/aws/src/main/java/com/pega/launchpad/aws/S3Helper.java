package com.pega.launchpad.aws;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Map;

public class S3Helper {

    public static Object listBucketsGsonBothWays(Map<String,String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .build()) {

            Gson g = new GsonBuilder().setExclusionStrategies(new FieldExclusionStrategy()).create();
            String json = g.toJson(s3.listBuckets());
            return g.fromJson(json, Map.class);
        }
    }

    public static String listBucketsJson(Map<String,String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .build()) {

            Gson g = new GsonBuilder().setExclusionStrategies(new FieldExclusionStrategy()).create();
            return g.toJson(s3.listBuckets());
        }
    }

    public static Object listBucketsPojo(Map<String,String> input) {

        System.setProperty("aws.accessKeyId", input.get("accessKeyId"));
        System.setProperty("aws.secretAccessKey", input.get("secretAccessKey"));

        try (S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .build()) {

            return s3.listBuckets();
        }
    }

    private static class FieldExclusionStrategy implements ExclusionStrategy {
        public FieldExclusionStrategy() {
        }

        public boolean shouldSkipField(FieldAttributes f) {
            if (f.getName().equals("responseMetadata")) {
                return true;
            } else {
                return false;
            }
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }


}
