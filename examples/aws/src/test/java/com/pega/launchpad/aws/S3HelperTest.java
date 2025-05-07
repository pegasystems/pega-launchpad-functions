package com.pega.launchpad.aws;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


class S3HelperTest {

    @Test
    void function() throws Exception {
        String accessKeyId = System.getenv("accessKeyId");
        String secretAccessKey = System.getenv("secretAccessKey");

        if (accessKeyId == null || secretAccessKey == null) return;

        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("accessKeyId", accessKeyId);
        inputMap.put("secretAccessKey", secretAccessKey);

        System.out.println(S3Helper.listBuckets(inputMap));

        String bucketName = "newbucket" + System.currentTimeMillis();
        inputMap.put("bucketName", bucketName);
        System.out.println(S3Helper.createBucket(inputMap));

        String fileName = "someobject" + System.currentTimeMillis();
        inputMap.put("objectKey", fileName);
        inputMap.put("objectBase64", "dGhpcyBpcyBhIHRlc3Q=");
        System.out.println(S3Helper.putObject(inputMap));

        inputMap.remove("objectBase64");

        System.out.println(S3Helper.getObject(inputMap));
    }
}