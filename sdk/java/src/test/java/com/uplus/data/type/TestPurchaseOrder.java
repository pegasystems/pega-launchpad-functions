package com.uplus.data.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPurchaseOrder {
    final ObjectMapper mapper = new ObjectMapper();
    final PurchaseOrder purchaseOrder = new PurchaseOrder("P#345",
            List.of(new PurchaseOrderItem("I#1A2B", 10.0, 5),
                    new PurchaseOrderItem("I#9Z8B", 4.0, 2)));


    @Test
    void deserialize() throws IOException {
        String json = Files.readString(Paths.get("src/test/resources/PurchaseOrder.json"));
        PurchaseOrder po = mapper.readValue(json, PurchaseOrder.class);
        assertEquals(purchaseOrder, po);
    }


    @Test
    void serialization() {
        assertDoesNotThrow(() -> mapper.writeValueAsString(purchaseOrder));
    }
}
