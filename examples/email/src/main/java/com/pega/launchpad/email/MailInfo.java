package com.pega.launchpad.email;

import java.util.ArrayList;

public class MailInfo {
    public String mailFrom;
    public String mailSubject;
    public String mailBody;
    public ArrayList<AttachmentInfo> attachments;

    public MailInfo(){
        this.mailFrom = "";
        this.mailSubject = "";
        this.mailBody = "";
        this.attachments = new ArrayList<>();
    }

    public MailInfo(String mailFrom, String mailSubject, String mailBody, ArrayList<AttachmentInfo> attachments){
        this.mailFrom = mailFrom;
        this.mailSubject = mailSubject;
        this.mailBody = mailBody;
        this.attachments = attachments;
    }
}