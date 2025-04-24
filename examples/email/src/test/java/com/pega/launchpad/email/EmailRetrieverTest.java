package com.pega.launchpad.email;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailRetrieverTest {

    @Test
    void retrieveEmailsMock() {
        EmailResponse response = EmailRetriever.retrieve("test", "test", "test", "test", "pop3s", 1, true, true);
        assertTrue(response.success);
        System.out.println(new Gson().toJson(response));
        assertNotNull(response.messages.get(0).receivedDate);
        assertEquals("[noreply@pega.com]", response.messages.get(0).from);
        assertEquals("mock", response.messages.get(0).subject);
        assertEquals("mock data", response.messages.get(0).body);
        assertEquals("mock.txt", response.messages.get(0).attachments.get(0).filename);
        assertEquals("bW9jaw==", response.messages.get(0).attachments.get(0).base64Content);
    }

    @Test
    void retrieveEmailsBadHost() {
        EmailResponse response = EmailRetriever.retrieve("test", "test", "test", "test", "pop3s", 1, false, false);
        assertFalse(response.success);
    }

}