package com.pega.lpst;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class Base64Test {

    @Test
    void decode() {
        Map<String,String> m = new HashMap<>();
        m.put("text", "aGVsbG8gd29ybGQ=");
        assertEquals("hello world", Base64.decode(m));
    }

    @Test
    void encode() {
        Map<String,String> m = new HashMap<>();
        m.put("text", "hello world");
        assertEquals("aGVsbG8gd29ybGQ=", Base64.encode(m));
    }
}