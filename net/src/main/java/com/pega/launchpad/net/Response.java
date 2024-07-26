package com.pega.launchpad.net;

import java.util.Map;
import com.google.gson.Gson;

/**
 * POJO for a REST response that contains both the response body (as a Map) and the response headers (as a Map of Strings)
 */
public class Response {
    public Object responseBody;
    public Map<String, String> responseHeaders;

    public Response(){}

    public String toString() {
        return  "Headers: " + new Gson().toJson(responseHeaders) + "\r\n" + "Body: " + new Gson().toJson(responseBody);
    }
}