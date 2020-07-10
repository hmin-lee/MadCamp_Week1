package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ImageDetailActivity extends Activity {
    boolean isPageOpen = false;

    Button button;
    LinearLayout slideDesc;

    Animation translateLeftAnim;
    Animation translateRightAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail);

        Intent intent = getIntent();

        int position = intent.getExtras().getInt("id");
        GalleryAdapter galleryAdapter = new GalleryAdapter(this);
        ImageView imageView = findViewById(R.id.image_detail);
        imageView.setImageResource(galleryAdapter.thumbImages[position]);

        slideDesc = findViewById(R.id.image_desc);
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_down);

        SlidingPageAnimationListener animationListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animationListener);
        translateRightAnim.setAnimationListener(animationListener);

        button = findViewById(R.id.image_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isPageOpen) {
                    slideDesc.startAnimation(translateRightAnim);
                } else {
                    slideDesc.setVisibility(View.VISIBLE);
                    slideDesc.startAnimation(translateLeftAnim);
                }
            }
        });
    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                slideDesc.setVisibility(View.INVISIBLE);

                button.setText("Open");
                isPageOpen = false;
            } else {
                button.setText("Close");
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
