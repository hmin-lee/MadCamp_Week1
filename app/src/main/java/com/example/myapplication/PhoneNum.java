package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class PhoneNum {
    private String userName;
    private String num;
    private Drawable  iconDrawable;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setUser(String user) {
        userName = user ;
    }
    public void setNum(String newnum) {
        num = newnum ;
    }

    public PhoneNum(Drawable  iconDrawable, String userName, String num){
        this.iconDrawable = iconDrawable;
        this.userName = userName;
        this.num = num;
    }

    public Drawable  getIcon() {
        return this.iconDrawable ;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getNum() {
        return this.num;
    }

}
