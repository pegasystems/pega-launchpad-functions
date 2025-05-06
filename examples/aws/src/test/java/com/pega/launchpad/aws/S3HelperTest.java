package com.pega.launchpad.aws;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


class S3HelperTest {

    @Test
    void function() {
        String accessKeyId = System.getenv("accessKeyId");
        String secretAccessKey = System.getenv("secretAccessKey");

        if (accessKeyId == null || secretAccessKey == null) return;

        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("accessKeyId", accessKeyId);
        inputMap.put("secretAccessKey", secretAccessKey);

        System.out.println(S3Helper.listBuckets(inputMap));

        inputMap.put("bucketName", "newbucket" + System.currentTimeMillis());
        System.out.println(S3Helper.createBucket(inputMap));

    }
}