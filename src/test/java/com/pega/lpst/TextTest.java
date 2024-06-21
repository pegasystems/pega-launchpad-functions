package com.pega.lpst;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {

    @Test
    void regex() {
        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("regex", "a*b");
        inputMap.put("text", "aaaaab");
        assertTrue(Text.regex(inputMap));
    }
}