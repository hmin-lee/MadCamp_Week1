package com.madcamp.parklee.tab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.madcamp.parklee.R;

import java.util.ArrayList;

public class ImageDetailActivity extends FragmentActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_activity);
        Intent intent = getIntent();
        int position = intent.getExtras().getInt("id");
        ArrayList<String> mImages = intent.getExtras().getStringArrayList("images");

        ViewPager vp = findViewById(R.id.image_pager);
        ImageDetailPagerAdapter adapter = new ImageDetailPagerAdapter(getSupportFragmentManager(), mImages);
        vp.setAdapter(adapter);
        vp.setCurrentItem(position);

    }
}

