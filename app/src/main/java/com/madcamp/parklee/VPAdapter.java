package com.madcamp.parklee;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.madcamp.parklee.tab1.Fragment1;
import com.madcamp.parklee.tab2.Fragment2;
import com.madcamp.parklee.tab3.Fragment3;

import java.util.ArrayList;

public class VPAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;
    private ArrayList<String> itext = new ArrayList<String>();

    public VPAdapter(FragmentManager fm) {
        super(fm);
        items = new ArrayList<Fragment>();
        items.add(new Fragment1());
        items.add(new Fragment2());
        items.add(new Fragment3());

        itext.add("Phone");
        itext.add("Gallery");
        itext.add("Diary");
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
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
