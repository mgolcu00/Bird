package com.example.bird.Models;

public class UserModel {
    String Id;
    String Username;
    String Lastname;
    String Email;
    String Password;
    String CreationDate;
    String ImageUrl;
    String PhoneNumber;
    String Status;
    String Location;

    public UserModel(String id, String username, String lastname, String email, String password, String creationDate, String imageUrl, String phoneNumber, String status, String location) {
        Id = id;
        Username = username;
        Lastname = lastname;
        Email = email;
        Password = password;
        CreationDate = creationDate;
        ImageUrl = imageUrl;
        PhoneNumber = phoneNumber;
        Status = status;
        Location = location;
    }

    public UserModel() {
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getId() {
        return Id;
    }

    public String getLastname() {
        return Lastname;
    }

    public String getEmail() {
        return Email;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public String getLocation() {
        return Location;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getStatus() {
        return Status;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
