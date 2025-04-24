package com.pega.launchpad.email;


import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class EmailRetriever {

    /**
     * Example of retrieve mail from a pop or imap server
     * @return An EmailResponse object which contains success/fail information and a list of messages
     */
    public static EmailResponse retrieve(String host, String port, String user, String password, String protocol, int maxCount, boolean unseenOnly, boolean mock) {
        EmailResponse response = new EmailResponse();
        response.success = true;
        response.errorMessage = "";
        response.messages = new ArrayList<EmailMetadata>();

        try {

            Properties properties = new Properties();
            properties.put("mail.store.protocol", protocol);
            properties.put(String.format("mail.%s.host", protocol), host);
            properties.put(String.format("mail.%s.port", protocol), port);
            properties.put(String.format("mail.%s.auth", protocol), "true");

            Session session = Session.getInstance(properties);


            if (mock) {
                EmailMetadata metadata = new EmailMetadata();
                metadata.receivedDate = new Date();
                metadata.from = Arrays.toString(new String[]{"noreply@pega.com"});
                metadata.subject = "mock";
                metadata.body = "mock data";
                metadata.attachments = new ArrayList<>();
                EmailMetadata.Attachment a = new EmailMetadata.Attachment();
                a.filename = "mock.txt";
                a.base64Content = "bW9jaw==";
                metadata.attachments.add(a);
                response.messages.add(metadata);
                return response;
            }

            try ( Store store = session.getStore(protocol)) {
                store.connect(host, user, password);

                try (Folder emailFolder = store.getFolder("INBOX")) {

                    emailFolder.open(Folder.READ_ONLY);

                    Message[] messages;

                    if (unseenOnly) {
                        Flags seen = new Flags(Flags.Flag.SEEN);
                        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);

                        messages = emailFolder.search(unseenFlagTerm);
                    } else {
                        messages = emailFolder.getMessages();
                    }

                    int count = 0;
                    for (Message message : messages) {
                        EmailMetadata metadata = new EmailMetadata();
                        metadata.receivedDate = message.getReceivedDate();
                        metadata.from = Arrays.toString(message.getFrom());
                        metadata.subject = message.getSubject();
                        metadata.body = getTextFromMessage(message);
                        metadata.attachments = getAttachmentsFromMessage(message);
                        response.messages.add(metadata);
                        if (++count >= maxCount) break;
                    }
                }
            }
            return response;
        } catch (Exception ex) {
            response.success = false;
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            response.errorMessage = ex.getMessage() + ": " + sw.toString();
            return response;
        }
    }

    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return "";
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append(html); // For simplicity, this example just appends HTML content
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    private static List<EmailMetadata.Attachment> getAttachmentsFromMessage(Message message) throws MessagingException, IOException {
        List<EmailMetadata.Attachment> attachments = new ArrayList<>();
        if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            for (int i = 0; i < mimeMultipart.getCount(); i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                    EmailMetadata.Attachment attachment = new EmailMetadata.Attachment();
                    attachment.filename = bodyPart.getFileName();
                    attachment.base64Content = Base64.getEncoder().encodeToString(bodyPart.getInputStream().readAllBytes());
                    attachments.add(attachment);
                }
            }
        }
        return attachments;
    }
}