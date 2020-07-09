package com.example.myapplication2;

import com.example.myapplication2.phoneBook.PhoneBook;
import com.example.myapplication2.phoneBook.UserInfo;

import org.junit.Test;

public class Fragment1Test {

    @Test
    public void getPhoneBookFromJson() {
        Fragment1 fragment1 = new Fragment1();
        PhoneBook phoneBook = fragment1.getPhoneBookFromJson();
        for (UserInfo userInfo : phoneBook.getUsers()) {
            System.out.println(userInfo.toString());
        }
        assert true;
    }
}