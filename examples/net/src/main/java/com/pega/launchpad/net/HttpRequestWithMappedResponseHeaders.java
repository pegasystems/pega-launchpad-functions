package com.pega.launchpad.net;

import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An example of a function that makes a REST call (GET, POST, PUT, PATCH) and then returns a Map containing both the response body (as a Map of objects) and the response HTTP headers (as a Map of strings)
 */
public class HttpRequestWithMappedResponseHeaders {

    /**
     * Send an HTTP request, and map the response body and headers into a Map that can be processed by a JSON transform
     *
     * @param inputMap Expects a key 'url', a key 'method' with one of these values: 'get', 'post', 'put', 'patch', 'delete', and a key 'body' with the JSON for the request body. Optionally you can provide http headers as json name-value pairs, with key 'headers'.
     * @return Response a POJO containing the responseBody and responseHeaders as a map
     * @throws IOException error with http request
     */
    public static Response send(Map<String, String> inputMap) throws IOException {

        // Default URL is a test URL, intended to be used with GET
        String url = inputMap.get("url");

        if (url == null) throw new IllegalArgumentException("url must be specified");

        // Default method is GET
        HttpUriRequestBase request;
        switch (inputMap.getOrDefault("method", "").strip().toLowerCase()) {
            case "post":
                request = new HttpPost(url);
                break;
            case "put":
                request = new HttpPut(url);
                break;
            case "patch":
                request = new HttpPatch(url);
                break;
            case "delete":
                request = new HttpDelete(url);
                break;
            default:
                request = new HttpGet(url);
                break;
        }

        // Set request timeout
        String timeout = inputMap.getOrDefault("connectionTimeout", "30000");
        request.
                setConfig(RequestConfig.copy(RequestConfig.DEFAULT).setConnectionRequestTimeout(Timeout.ofMilliseconds(Integer.parseInt(timeout))).build());

        // Set request headers
        String reqHeaders = inputMap.getOrDefault("headers", "");
        if (!reqHeaders.isEmpty()) {
            // Manually parse JSON string key-value pairs, then add each header name + value to the request
            @SuppressWarnings("unchecked") Map<String, String> map = new Gson().fromJson(reqHeaders, HashMap.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // Set request body - only necessary for POST/PUT/PATCH, if provided
        if (!(request instanceof HttpGet)) {
            String reqBody = inputMap.getOrDefault("body", "");
            if (!reqBody.isEmpty()) {
                HttpEntity entity = EntityBuilder.create().setText(reqBody).build();
                request.setEntity(entity);
            }
        }

        // Execute the request
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            return httpClient.execute(request, response -> {
                Response localResponse = new Response();

                try (HttpEntity entity1 = response.getEntity()) {
                    // Get the response status

                    localResponse.responseStatus = new HashMap<>();
                    localResponse.responseStatus.put("statusCode", Integer.toString(response.getCode()));
                    localResponse.responseStatus.put("reason", response.getReasonPhrase());
                    localResponse.responseStatus.put("protocolName", response.getVersion().getProtocol());
                    localResponse.responseStatus.put("protocolMajorVersion", Integer.toString(response.getVersion().getMajor()));
                    localResponse.responseStatus.put("protocolMinorVersion", Integer.toString(response.getVersion().getMinor()));

                    // Get response headers out of response, then construct a map out of them
                    Header[] allHeaders = response.getHeaders();
                    localResponse.responseHeaders = new HashMap<>();
                    for (Header header : allHeaders) {
                        localResponse.responseHeaders.put(header.getName(), header.getValue());
                    }

                    byte[] responseBody = entity1.getContent().readAllBytes();
                    if (responseBody == null || responseBody.length == 0) {
                        localResponse.responseBody = new Object();
                    } else {
                        String responseBodyString = new String(responseBody).strip();
                        if (responseBodyString.startsWith("[")) {
                            localResponse.responseBody = new Gson().fromJson(responseBodyString, List.class);
                        } else if (responseBodyString.startsWith("{")) {
                            localResponse.responseBody = new Gson().fromJson(responseBodyString, Map.class);
                        } else {
                            localResponse.responseBody = new Object();
                        }
                    }
                }
                return localResponse;
            });
        }
    }
}