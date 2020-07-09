package com.example.myapplication2.phoneBook;

public class UserInfo {
    private String number;
    private String name;
    private String pictureId;

    public UserInfo(String name, String number, String pictureId) {
        this.name = name;
        this.number = number;
        this.pictureId = pictureId;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getPictureId() {
        return pictureId;
    }
}
//TODO: builder 패턴으로 수정하기