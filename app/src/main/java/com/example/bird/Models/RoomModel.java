package com.example.bird.Models;

public class RoomModel {
    private String RoomName;
    private int UserCount;
    private String id;

//empty consturaction
    public RoomModel() {
    }


    public RoomModel(String id, int c, String n) {
        this.id = id;
        this.UserCount = c;
        this.RoomName = n;
    }

    public String getRoomName() {
        return RoomName;
    }

    public String getRoomId() {
        return id;
    }

    public int getRoomCount() {
        return UserCount;
    }

    public void setRoomId(String id) {
        this.id = id;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public void setUserCount(int userCount) {
        UserCount = userCount;
    }


}

