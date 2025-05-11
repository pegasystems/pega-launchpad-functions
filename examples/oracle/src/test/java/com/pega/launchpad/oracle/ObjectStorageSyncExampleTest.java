package com.pega.launchpad.oracle;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class ObjectStorageSyncExampleTest {

    @Test
    void test()  {
        String privateKeyBase64 = System.getenv("privateKeyBase64");
        if (privateKeyBase64 == null) {
            System.out.println("*** env variables not configured, skipping tests");
            return;
        }

        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("privateKeyBase64", privateKeyBase64);
        inputMap.put("userId", System.getenv("userId"));
        inputMap.put("tenantId", System.getenv("tenantId"));
        inputMap.put("fingerprint", System.getenv("fingerprint"));

        inputMap.put("bucketName", "bucket-" + System.currentTimeMillis());
        inputMap.put("compartmentId", inputMap.get("tenantId"));

        System.out.println(new Gson().toJson(ObjectStorageSyncExample.createBucket(inputMap)));

        System.out.println(new Gson().toJson(ObjectStorageSyncExample.listBuckets(inputMap)));

        inputMap.put("objectName", "object-" + System.currentTimeMillis());
        inputMap.put("objectBase64", "dGhpcyBpcyBhIHRlc3Q=");
        System.out.println(new Gson().toJson(ObjectStorageSyncExample.putObject(inputMap)));

        inputMap.remove("objectBase64");
        System.out.println(ObjectStorageSyncExample.getObject(inputMap));

    }
}