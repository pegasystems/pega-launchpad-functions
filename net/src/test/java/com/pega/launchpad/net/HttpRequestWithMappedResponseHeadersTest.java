package com.pega.launchpad.net;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class HttpRequestWithMappedResponseHeadersTest {

    @Test
    void send() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("method", "post");
        inputMap.put("url", "https://my.api.mockaroo.com/CustomerRegistration?key=5828ac60");
        inputMap.put("body", "{ \"first_name\":\"Sibley\", \"last_name\":\"Berthod\", \"address\":\"513 Talisman Crossing\", \"city\":\"Mirões\", \"state\":\"Aveiro\", \"country\":\"Portugal\", \"email\":\"sberthod0@merriam-webster.com\" }");
        Response r = HttpRequestWithMappedResponseHeaders.send(inputMap);
        System.out.println("send: " + r);
    }

    @Test
    void sendNoResponseBody() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("method", "post");
        inputMap.put("url", "https://my.api.mockaroo.com/EmptyResponseBody?key=5828ac60&__method=POST");
        inputMap.put("body", "{}");
        Response r = HttpRequestWithMappedResponseHeaders.send(inputMap);
        System.out.println("sendNoResponseBody: " + r);
    }

    @Test
    void sendWithGet() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("method", "get");
        inputMap.put("url", "https://my.api.mockaroo.com/Company?key=5828ac60");
        inputMap.put("body", "{}");
        Response r = HttpRequestWithMappedResponseHeaders.send(inputMap);
        System.out.println("sendWithGet:" + r);
    }

    @Test
    void getLyrics() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("method", "get");
        inputMap.put("url", "https://api.lyrics.ovh/v1/beatles/Yesterday");
        inputMap.put("body", "{}");
        Response r = HttpRequestWithMappedResponseHeaders.send(inputMap);
        System.out.println("getLyrics:" + r);
    }
}