package com.pega.launchpad.geocoding;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class GeocodingTest {

    @Test
    void getAddressComponents() {

        Map<String,String> inputMap = new HashMap<>();
        inputMap.put("key", "1234");
        inputMap.put("address", "21 Beech ST, North Chelmsford, MA 01863");
        try {
            System.out.println(Geocoding.getAddressComponents(inputMap));
        } catch (RuntimeException ex) {
            // ok
        }
    }
}