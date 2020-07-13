package com.example.myapplication;

public class PhoneNum {
    private String userName;
    private String num;
    private int iconDrawable;

    public PhoneNum(int iconDrawable, String userName, String num) {
        this.iconDrawable = iconDrawable;
        this.userName = userName;
        this.num = num;
    }

    public PhoneNum(String userName, String num) {
        this.iconDrawable = R.drawable.baseline_account_box_black_18dp;
        this.userName = userName;
        this.num = num;
    }

    public void setUser(String user) {
        userName = user;
    }

    public int getIcon() {
        return this.iconDrawable;
    }

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
