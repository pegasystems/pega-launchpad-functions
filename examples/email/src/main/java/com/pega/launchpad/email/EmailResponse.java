package com.pega.launchpad.email;

import java.util.List;

public class EmailResponse {
    public boolean success;
    public String errorMessage;
    public List<EmailMetadata> messages;
}
