package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class ImageDetailActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail);

        Intent intent = getIntent();

        int position = intent.getExtras().getInt("id");
        GalleryAdapter galleryAdapter = new GalleryAdapter(this);
        ImageView imageView = findViewById(R.id.image_detail);
        imageView.setImageResource(galleryAdapter.thumbImages[position]);


    }
}
