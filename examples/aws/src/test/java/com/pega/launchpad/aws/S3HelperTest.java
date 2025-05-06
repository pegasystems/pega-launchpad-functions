package com.pega.launchpad.aws;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


class S3HelperTest {

    @Test
    void function() throws ClassNotFoundException, NoSuchFieldException {

        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("accessKeyId", "");
        inputMap.put("secretAccessKey", "");

        //System.out.println(S3Helper.listBucketsGsonBothWays(inputMap));
    }
}