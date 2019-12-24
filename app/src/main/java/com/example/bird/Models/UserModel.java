package com.example.bird.Models;

public class UserModel {
    String name;
    String email;
    String pass;
    String lastname;
    String CreationDate;
    String ImageUrl;

    public UserModel(){

    }
    public UserModel(String n, String l){
        this.name=n;
        this.lastname=l;
    }

    public UserModel(String n, String e, String p, String l, String d) {
        this.name = n;
        this.email = e;
        this.pass = p;
        this.lastname = l;
        this.CreationDate = d;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getLastname() {
        return lastname;
    }
}
