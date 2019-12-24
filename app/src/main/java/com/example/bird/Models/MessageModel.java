package com.example.bird.Models;

public class MessageModel {
    private String id;
    private String text;
    private String name;
    private String ppUrl;
    private String UserId;
    private String DateTime;



    public MessageModel(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public MessageModel() {

    }


    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getPpUrl() {
        return ppUrl;
    }

    public String getUserId() {
        return UserId;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPpUrl(String ppUrl) {
        this.ppUrl = ppUrl;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }
}
