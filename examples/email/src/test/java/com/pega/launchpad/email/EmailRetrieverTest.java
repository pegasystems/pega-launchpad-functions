package com.pega.launchpad.email;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

class EmailRetrieverTest {

    @Test
    void retrieveEmails() {
        EmailResponse response = EmailRetriever.retrieve("test", "test", "test", "test", "pop3s", 1, true);
        String json = new Gson().toJson(response);
        System.out.println(json);
    }
}