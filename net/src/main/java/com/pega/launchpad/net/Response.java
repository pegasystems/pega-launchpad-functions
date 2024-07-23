package com.pega.launchpad.net;

import java.util.Map;

/**
 * POJO for a REST response that contains both the response body (as a Map) and the response headers (as a Map of Strings)
 */
public class Response {
    public Map<?, ?> responseBody;
    public Map<String, String> responseHeaders;

    public Response(){}
}