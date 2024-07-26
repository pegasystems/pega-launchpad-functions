package com.pega.launchpad.net;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An example of a function that makes a REST call (GET, POST, PUT, PATCH) and then returns a Map containing both the response body (as a Map of objects) and the response HTTP headers (as a Map of strings)
 */
public class HttpRequestWithMappedResponseHeaders {

    /**
     * Send an HTTP request, and map the response body and headers into a Map that can be processed by a JSON transform
     * @param inputMap Expects a key 'url', a key 'method' with one of these values: 'post', 'put', 'patch', 'delete', and a key 'body' with the JSON for the request body. Optionally you can provide http headers as json name-value pairs, with key 'headers'.
     * @return Response a POJO containing the responseBody and responseHeaders as a map
     * @throws IOException error with http request
     */
    public static Response send(Map<String, String> inputMap) throws IOException {
        Response CFresponse = new Response();

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature());

        // Default URL is a test URL, intended to be used with GET
        String url = inputMap.get("url");

        if (url == null) throw new IllegalArgumentException("url must be specified");

        // Default method is GET
        HttpRequestBase request;
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

        // Set request headers
        String reqHeaders = inputMap.getOrDefault("headers", "");
        if (!reqHeaders.isEmpty()) {
            // Manually parse JSON string key-value pairs, then add each header name + value to the request
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {
            };
            Map<String, String> map = mapper.readValue(reqHeaders, typeRef);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // Set request body - only necessary for POST/PUT/PATCH, if provided
        String reqBody = inputMap.getOrDefault("body", "");
        if (!reqBody.isEmpty()) {
            if (request instanceof HttpPost) {
                ((HttpPost) request).setEntity(new StringEntity(reqBody));
            } else if (request instanceof HttpPut) {
                ((HttpPut) request).setEntity(new StringEntity(reqBody));
            } else if (request instanceof HttpPatch) {
                ((HttpPatch) request).setEntity(new StringEntity(reqBody));
            }
        }

        // Execute the request
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response1 = httpClient.execute(request)) {
                HttpEntity entity1 = response1.getEntity();

                // Get response headers out of response, then construct a map out of them
                Header[] allHeaders = response1.getAllHeaders();
                CFresponse.responseHeaders = new HashMap<>();
                for (Header header : allHeaders) {
                    CFresponse.responseHeaders.put(header.getName(), header.getValue());
                }

                TypeReference<Map<?, ?>> typeRef = new TypeReference<>() {};
                byte[] responseBody = entity1.getContent().readAllBytes();
                if (responseBody == null || responseBody.length == 0) responseBody = "{}".getBytes();
                CFresponse.responseBody = mapper.readValue(responseBody, typeRef);


                EntityUtils.consume(entity1);
            }
        }

        return CFresponse;
    }
}