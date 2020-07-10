package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ImageDetailActivity extends Activity {
    boolean isPageOpen = false;

    Button button;
    LinearLayout slideDesc;

    GestureDetector gestureDetector;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    Animation translateLeftAnim;
    Animation translateRightAnim;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail);
        gestureDetector = new GestureDetector(this, new GestureListener());
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

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean touchEvent = gestureDetector.onTouchEvent(motionEvent);
                if (touchEvent){
                    Toast.makeText(getApplicationContext(), "플링함", Toast.LENGTH_LONG).show();
                    finish();
                }
                if (isPageOpen) {
                    slideDesc.startAnimation(translateRightAnim);
                } else {
                    slideDesc.setVisibility(View.VISIBLE);
                    slideDesc.startAnimation(translateLeftAnim);
                }
                return true;
            }


        });


    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                slideDesc.setVisibility(View.INVISIBLE);
                isPageOpen = false;
            } else {
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

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Left to right
            }

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return true; // Top to bottom
            }
            return false;
        }
    }
}

