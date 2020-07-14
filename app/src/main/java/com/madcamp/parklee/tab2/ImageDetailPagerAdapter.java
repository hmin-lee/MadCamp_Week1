package com.madcamp.parklee.tab2;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class ImageDetailPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "ImageDetailPagerAdapter";
    public ArrayList<String> thumbImages;
    private ArrayList<Fragment> items;

    public ImageDetailPagerAdapter(@NonNull FragmentManager fm, ArrayList<String> mImages) {
        super(fm);
        items = new ArrayList<>();
        thumbImages = mImages;
        for (int i = 0; i < thumbImages.size(); i++) {
            File file = new File(thumbImages.get(i));
            ImageInfo imageInfo = new ImageInfo.Builder(thumbImages.get(i)).setTitle(file.getName()).setDatetime(new Date(file.lastModified()).toString()).build();
            Log.d(TAG, "ImageDetailPagerAdapter: Constructor");
            items.add(new ImageDetailFragment(imageInfo));
        }

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

}
