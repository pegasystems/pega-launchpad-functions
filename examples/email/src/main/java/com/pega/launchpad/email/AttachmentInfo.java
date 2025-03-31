package com.pega.launchpad.email;

public class AttachmentInfo {
    public String fileName;
    public String base64Content;

    public AttachmentInfo(){
        this.fileName="";
        this.base64Content="";
    }

    public AttachmentInfo(String fileName, String base64Content){
        this.fileName = fileName;
        this.base64Content = base64Content;
    }
}