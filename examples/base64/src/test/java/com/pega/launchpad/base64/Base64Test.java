package com.pega.launchpad.base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64Test {

    @Test
    void decode() {
        Map<String, String> m = new HashMap<>();
        m.put("text", "aGVsbG8gd29ybGQ=");
        Assertions.assertEquals("hello world", Base64.decode(m));
    }

    @Test
    void encode() {
        Map<String, String> m = new HashMap<>();
        m.put("text", "hello world");
        assertEquals("aGVsbG8gd29ybGQ=", Base64.encode(m));
    }
}