package com.example.bird.Post;

import com.example.bird.Login.UserC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class PostData {
    String CreatingDate;
    String Posttext;
    String PostImageUrl;
    UserC user;

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

    public UserC getUser() {
        return user;
    }


    public void setUser(UserC user) {
        this.user = user;
    }

    public static Comparator<PostData> pstDate = new Comparator<PostData>() {

        public int compare(PostData s1, PostData s2) {

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
