package com.pega.lpst;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class Base64Test {

    @Test
    void decode() {
        Map<Object,Object> m = new HashMap<Object,Object>();
        m.put("text", "aGVsbG8gd29ybGQ=");
        assertEquals("hello world", (String)Base64.decode(m));
    }

    @Test
    void encode() {
        Map<Object,Object> m = new HashMap<Object,Object>();
        m.put("text", "hello world");
        assertEquals("aGVsbG8gd29ybGQ=", (String)Base64.encode(m));
    }
}