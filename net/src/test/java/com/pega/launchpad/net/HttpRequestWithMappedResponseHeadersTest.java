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
        System.out.println(r);
    }
}