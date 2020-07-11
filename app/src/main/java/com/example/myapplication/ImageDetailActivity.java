package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class ImageDetailActivity extends FragmentActivity {

    ImageDetailFragment imageDetailFragment;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_activity);
        Intent intent = getIntent();
        int position = intent.getExtras().getInt("id");

        ViewPager vp = findViewById(R.id.image_pager);
        ImageDetailPagerAdapter adapter = new ImageDetailPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setCurrentItem(position);

    }
}

