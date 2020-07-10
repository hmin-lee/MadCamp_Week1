package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class FullImageActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);

        Intent intent = getIntent();

        int position = intent.getExtras().getInt("id");
        GalleryAdapter galleryAdapter = new GalleryAdapter(this);
        ImageView imageView = findViewById(R.id.full_image_view);
        imageView.setImageResource(galleryAdapter.thumbImages[position]);


    }
}
