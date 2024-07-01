package com.pega.launchpad;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import java.util.Map;

public class PDFfromURLReturnStringHandler implements RequestHandler<Map<Object,Object>, Object> {
    @Override
    public String handleRequest(Map<Object, Object> stringStringMap, Context context) {
        String body = (String)stringStringMap.get("body");
        Map<String,String> map = new Gson().fromJson(body, Map.class);
        return PDF.setFieldsWithURL(map);
    }
}
