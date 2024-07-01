package com.pega.launchpad;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import java.util.Map;

public class PDFfromURLReturnJsonHandler implements RequestHandler<Map<Object,Object>, String> {
    @Override
    public String handleRequest(Map<Object, Object> stringStringMap, Context context) {
        String body = (String)stringStringMap.get("body");
        Map<String,String> map = new Gson().fromJson(body, Map.class);
        return "{\"base64\":\"" + PDF.setFieldsWithURL(map) + "\"}";
    }
}
