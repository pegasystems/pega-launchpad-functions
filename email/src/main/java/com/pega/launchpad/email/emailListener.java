package com.pega.launchpad.email;

import java.util.*;
import com.sun.mail.util.BASE64DecoderStream;
import jakarta.mail.*;
import jakarta.mail.Flags.Flag;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.search.*;


public class emailListener {

    public static ArrayList<MailInfo> check(Map<String, String> inputs) {

        //Define list of mails
        ArrayList<MailInfo> Mails = new ArrayList<>();

        try {
            String host = inputs.get("host");
            String port = inputs.getOrDefault("port", "993");
            String storeType = inputs.get("storeType");
            String user = inputs.get("user");
            String password = inputs.get("password");

            // create properties
            Properties properties = new Properties();

            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", port);
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);
            properties.put("mail.imap.partialfetch","false");

            Session emailSession = Session.getDefaultInstance(properties);

            // create the imap store object and connect to the imap server
            Store store = emailSession.getStore(storeType);
            store.connect(host, user, password);

            // create the inbox object and open it
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);

            //create a search term for all "unseen" messages
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm flagTerm = new FlagTerm(seen, false);


            // retrieve the messages from the folder in an array and print it
            Message[] messages = inbox.search(flagTerm);
            int emailsCount = messages.length;

            for (int i = emailsCount; i > 0; i--) {

                //Define attachments
                ArrayList<AttachmentInfo> mailAttachments = new ArrayList<>();

                Message message = messages[i-1];

                //Get the subject of mail
                String mailSubject = message.getSubject();
                System.out.println("mail subject is:"+mailSubject);

                //Initialize message content
                String messageContent="";

                //Get the from address
                Address[] froms = message.getFrom();
                String mailFrom = froms == null ? null : ((InternetAddress) froms[0]).getAddress();

                //Reading content and attachments
                String contentType = message.getContentType();
                if (contentType.contains("multipart")) {
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();

                            BASE64DecoderStream decodeStream = (BASE64DecoderStream) part.getContent();

                            // Read all the bytes from the array in an ever-growing buffer.
                            // Didn't use any utils for minimum dependencies. Don't to this at home!
                            byte[] data = new byte[1024];
                            int count = decodeStream.read(data);
                            int startPos;
                            while(count == 1024) {
                                byte[] addBuffer = new byte[data.length + 1024];
                                System.arraycopy(data, 0, addBuffer, 0, data.length);
                                startPos = data.length;
                                data = addBuffer;
                                count = decodeStream.read(data, startPos, 1024);
                            }
                            String result = Base64.getEncoder().encodeToString(data);
                            mailAttachments.add(new AttachmentInfo(fileName, result));

                        } else {
                            // this part may be the message content
                            if(part.isMimeType("text/*")){
                                //Handling plain messages
                                messageContent = part.getContent().toString();
                            } else if(part.isMimeType("multipart/alternative")) {
                                //Handling multipart alternative
                                Multipart newMP = (Multipart)part.getContent();
                                for(int j=0; j<newMP.getCount(); j++){
                                    Part bp = newMP.getBodyPart(j);
                                    messageContent = bp.getContent().toString();
                                }
                            } else {
                                //Others if present
                                messageContent = part.getContent().toString();
                            }
                        }
                    }

                }else if (contentType.toLowerCase().contains("text/plain")
                        || contentType.toLowerCase().contains("text/html")) {
                    Object content = message.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }

                //Marks the mails as seen so that they are not fetched from next time
                message.setFlag(Flag.SEEN, true);

                Mails.add(new MailInfo(mailFrom, mailSubject, messageContent, mailAttachments));

            }

            inbox.close(true);
            store.close();

            return Mails;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}