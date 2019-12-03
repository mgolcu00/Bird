package com.example.bird.Post;

import android.media.Image;

import com.example.bird.Login.UserC;

public class PostData {
    String Posttext;
    //Image image;
    UserC user;

    public String getPosttext() {
        return Posttext;
    }

    public void setPosttext(String posttext) {
        Posttext = posttext;
    }

    public UserC getUser() {
        return user;
    }


    public void setUser(UserC user) {
        this.user = user;
    }
}
