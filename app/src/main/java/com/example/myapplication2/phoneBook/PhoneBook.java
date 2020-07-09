package com.example.myapplication2.phoneBook;

import com.google.gson.Gson;

import java.util.ArrayList;

public class PhoneBook {
    private ArrayList<UserInfo> users;

    public PhoneBook() {
        users = new ArrayList<UserInfo>();
    }

    public ArrayList<UserInfo> getUsers() {
        return users;
    }

    public UserInfo getUserInfoAt(int position) {
        return users.get(position);
    }

    public void addPhoneNumber(String name, String number, String picture_id) {
        users.add(new UserInfo(name, number, picture_id));
    }

    public ArrayList<UserInfo> parseUserInfoList() {
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        Gson gson = new Gson();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfos;
    }

    public void addUserInfo(UserInfo userInfo) {
        users.add(userInfo);
    }
}
