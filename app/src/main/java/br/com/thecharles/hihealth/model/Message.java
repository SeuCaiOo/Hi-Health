package br.com.thecharles.hihealth.model;

public class Message {

    private String idSender;
    private String message;
    private String file;

    public Message() {
    }

    public String getIdSender() {
        return idSender;
    }

    public Message setIdSender(String idSender) {
        this.idSender = idSender;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getFile() {
        return file;
    }

    public Message setFile(String file) {
        this.file = file;
        return this;
    }
}