package com.pega.launchpad.aws;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class KMSHelperTest {

    @Test
    public void test() {

        String accessKeyId = System.getenv("accessKeyId");
        String secretAccessKey = System.getenv("secretAccessKey");

        if (accessKeyId == null || secretAccessKey == null) {
            System.err.println("AWS access keys not provided, skipping tests.");
            return;
        }

        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("accessKeyId", accessKeyId);
        inputMap.put("secretAccessKey", secretAccessKey);
        inputMap.put("text", "this is a test");
        inputMap.put("keyId", "keyid");
        inputMap.put("region", "us-west-2");

        String encrypt = KMSHelper.encrypt(inputMap);
        System.out.println(encrypt);
        inputMap.put("text", encrypt);

        System.out.println(KMSHelper.decrypt(inputMap));
    }
}