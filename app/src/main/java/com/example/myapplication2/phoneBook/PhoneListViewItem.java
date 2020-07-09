package com.example.myapplication2.phoneBook;

import android.graphics.drawable.Drawable;

public class PhoneListViewItem {
    private Drawable iconDrawable;
    private String nameString;
    private String numberString;

    public void setIconDrawable(Drawable iconDrawable){
        this.iconDrawable = iconDrawable;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public String getNameString() {
        return nameString;
    }

    public String getNumberString() {
        return numberString;
    }
}
