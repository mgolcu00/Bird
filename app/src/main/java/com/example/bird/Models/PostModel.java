package com.example.bird.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class PostModel {
    String UserId;
    String PostId;
    String CreatingDate;
    String EventDate;
    String EventLocation;
    String Posttext;
    String PostImageUrl;
    UserModel user;
    public PostModel(){

    }
    public PostModel(String userId, String postId, String creatingDate, String eventDate, String eventLocation, String posttext, String postImageUrl, UserModel user) {
        UserId = userId;
        PostId = postId;
        CreatingDate = creatingDate;
        EventDate = eventDate;
        EventLocation = eventLocation;
        Posttext = posttext;
        PostImageUrl = postImageUrl;
        this.user = user;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getEventLocation() {
        return EventLocation;
    }

    public void setEventLocation(String eventLocation) {
        EventLocation = eventLocation;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostImageUrl() {
        return PostImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        PostImageUrl = postImageUrl;
    }

    public String getCreatingDate() {
        return CreatingDate;
    }

    public void setCreatingDate(String creatingDate) {
        CreatingDate = creatingDate;
    }

    public String getPosttext() {
        return Posttext;
    }

    public void setPosttext(String posttext) {
        Posttext = posttext;
    }

    public UserModel getUser() {
        return user;
    }


    public void setUser(UserModel user) {
        this.user = user;
    }

    public static Comparator<PostModel> pstDate = new Comparator<PostModel>() {

        public int compare(PostModel s1, PostModel s2) {

            String date1 = s1.getCreatingDate().toUpperCase();
            String date2 = s2.getCreatingDate().toUpperCase();

            Date datex1 = new Date(), datex2 = new Date();
            datex1.setTime(datex1.getTime());
            datex2.setTime(datex2.getTime());
            SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy");
            try {
                datex1 = format.parse(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                datex2 = format.parse(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            /*For ascending order*/
            assert datex1 != null;
            return datex2.compareTo(datex1);

            /*For descending order*/
            //rollno2-rollno1;
        }
    };

}
