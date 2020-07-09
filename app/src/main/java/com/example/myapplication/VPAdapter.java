package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;
    private ArrayList<String> itext = new ArrayList<String>();

    public VPAdapter(FragmentManager fm){
        super(fm);
        items = new ArrayList<Fragment>();
        items.add(new Fragment1());
        items.add(new fragment2());
        items.add(new Fragment3());

        itext.add("전화번호");
        itext.add("사진");
        itext.add("몰랑");
    }
    @NonNull
    @Override
    public  CharSequence getPageTitle (int position) {
        return itext.get(position);
    }

    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
