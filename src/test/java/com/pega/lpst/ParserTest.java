package com.pega.lpst;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void fromDelimitedText() {
        Map<Object,Object> m = new HashMap<Object,Object>();
        m.put("text", "tim,frank,parag");
        List<Map> l = (List<Map>)Parser.fromDelimitedText(m);
        assertNotNull(l);
        assertEquals(3, l.size());

        assertTrue(l.get(0).containsKey("token"));
        assertEquals("tim", l.get(0).get("token"));
        assertTrue(l.get(1).containsKey("token"));
        assertEquals("frank", l.get(1).get("token"));
        assertTrue(l.get(2).containsKey("token"));
        assertEquals("parag", l.get(2).get("token"));
    }

    @Test
    void fromCsv() {
        Map<Object,Object> m = new HashMap<Object,Object>();
        m.put("csv", "name,city,state\nTim,Chelmsford,MA\nGabe,Arlington,MA\n");
        List<Map> l = (List<Map>)Parser.fromCsv(m);
        assertNotNull(l);
        assertEquals(2, l.size());

        assertTrue(l.get(0).containsKey("name"));
        assertTrue(l.get(0).containsKey("city"));
        assertTrue(l.get(0).containsKey("state"));
        assertEquals("Tim", l.get(0).get("name"));
        assertEquals("Chelmsford", l.get(0).get("city"));
        assertEquals("MA", l.get(0).get("state"));

        assertTrue(l.get(1).containsKey("name"));
        assertTrue(l.get(1).containsKey("city"));
        assertTrue(l.get(1).containsKey("state"));
        assertEquals("Gabe", l.get(1).get("name"));
        assertEquals("Arlington", l.get(1).get("city"));
        assertEquals("MA", l.get(1).get("state"));

    }
}