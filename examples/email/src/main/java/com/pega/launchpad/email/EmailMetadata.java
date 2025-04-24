package com.pega.launchpad.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class EmailMetadata {
    public Date receivedDate;
    public String from;
    public String subject;
    public String body;
    public List<Attachment> attachments = new ArrayList<>();

    public static class Attachment {
        public String filename;
        public String base64Content;
    }
}
