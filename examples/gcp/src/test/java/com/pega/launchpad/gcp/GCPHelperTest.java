package com.pega.launchpad.gcp;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class GCPHelperTest {

    @Test
    public void test() throws Exception {

        String creds = System.getenv("base64EncodedJsonCredentials");

        if (creds == null) return; // comment this out to unit test with your own credentials

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("base64EncodedJsonCredentials", creds);

        String bucketName = "com-pega-launchpad-gcp-bucket" + System.currentTimeMillis();
        String objectName = "object" + System.currentTimeMillis();
        String objectBase64 = "dGhpcyBpcyBhIHRlc3Q=";

        inputMap.put("bucketName", bucketName);
        System.out.println(new Gson().toJson(GCPHelper.createBucket(inputMap)));

        inputMap.put("objectName", objectName);
        inputMap.put("objectBase64", objectBase64);

        System.out.println(new Gson().toJson(GCPHelper.createObject(inputMap)));

        inputMap.remove("objectBase64");

        System.out.println(GCPHelper.getObject(inputMap));
    }
}