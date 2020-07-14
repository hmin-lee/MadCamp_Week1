package com.example.myapplication;

public class PhoneNum {
    private String userName;
    private String num;
    private int iconDrawable = 0;
    private String iconUri = "a";

    public PhoneNum(int iconDrawable, String userName, String num) {
        this.iconDrawable = iconDrawable;
        this.userName = userName;
        this.num = num;
    }
    public PhoneNum(String iconUri, String userName, String num) {
        this.iconUri = iconUri;
        this.userName = userName;
        this.num = num;
    }
    public PhoneNum(int iconDrawable, String iconUri, String userName, String num) {
        this.iconDrawable = iconDrawable;
        this.iconUri = iconUri;
        this.userName = userName;
        this.num = num;
    }

    public void setUser(String user) {
        userName = user;
    }

    public int getIcon() {
        return this.iconDrawable;
    }

    public String getIconUri() {return this.iconUri; }

    public void setIcon(int icon) {
        iconDrawable = icon;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String newnum) {
        num = newnum;
    }


}
