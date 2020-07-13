package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ImageDetailPagerAdapter extends FragmentPagerAdapter {
    public ArrayList<String> thumbImages;
//    public Integer[] thumbImages = {
//            R.drawable.img1, R.drawable.img2, R.drawable.img3,
//            R.drawable.img4, R.drawable.img5, R.drawable.img6,
//            R.drawable.img7, R.drawable.img8, R.drawable.img9,
//            R.drawable.img10, R.drawable.img11, R.drawable.img12,
//            R.drawable.img13, R.drawable.img1, R.drawable.img2, R.drawable.img3,
//            R.drawable.img4, R.drawable.img5, R.drawable.img6,
//            R.drawable.img7, R.drawable.img8, R.drawable.img9,
//    };
    private ArrayList<Fragment> items;

    public ImageDetailPagerAdapter(@NonNull FragmentManager fm, ArrayList<String> mImages) {
        super(fm);
        items = new ArrayList<>();
        thumbImages = mImages;
        for (int i = 0; i < thumbImages.size(); i++) {
            ImageInfo imageInfo = new ImageInfo.Builder(thumbImages.get(i)).setTitle("제목" + i + ".jpg").setDatetime("2020.07.11").build();
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
