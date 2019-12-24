package com.example.bird.Chat;

public class Messeage {
    private String id;
    private String text;
    private String name;
    private String ppUrl;



    public Messeage(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public Messeage() {

    }

    public String getPpUrl() {
        return ppUrl;
    }

    public void setPpUrl(String ppUrl) {
        this.ppUrl = ppUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
