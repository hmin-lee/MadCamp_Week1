package com.madcamp.parklee.tab2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.madcamp.parklee.R;

import java.util.Objects;

public class ImageDetailFragment extends Fragment {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    LinearLayout slideDesc;

    GestureDetector gestureDetector;
    Context myContext;
    boolean isPageOpen = false;
    Animation translateUpAnim;
    Animation translateDownAnim;
    private String CUR_IMG = "";
    private ImageInfo CUR_INFO;

    public ImageDetailFragment(ImageInfo info) {
        CUR_IMG = info.getResId();
        CUR_INFO = info;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.image_detail_fragment, container, false);
        myContext = getContext();
        ImageView imageView = myView.findViewById(R.id.image_detail_fragment);
        TextView textViewTitle = myView.findViewById(R.id.image_detail_title);
        TextView textViewDatetime = myView.findViewById(R.id.image_detail_datetime);

        Glide.with(myContext).load(CUR_IMG).into(imageView);
        textViewTitle.setText(CUR_INFO.getTitle());
        textViewDatetime.setText(CUR_INFO.getDatetime());

        gestureDetector = new GestureDetector(myContext, new ImageDetailFragment.GestureListener());
        slideDesc = myView.findViewById(R.id.image_desc_fragment);
        translateUpAnim = AnimationUtils.loadAnimation(myContext, R.anim.translate_up);
        translateDownAnim = AnimationUtils.loadAnimation(myContext, R.anim.translate_down);
        ImageDetailFragment.SlidingPageAnimationListener animationListener = new ImageDetailFragment.SlidingPageAnimationListener();
        translateUpAnim.setAnimationListener(animationListener);
        translateDownAnim.setAnimationListener(animationListener);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean touchEvent = gestureDetector.onTouchEvent(motionEvent);
                if (touchEvent) {
                    System.out.println("Logic is Migrated to Gesture");
                }
                return true;
            }
        });

        return myView;
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
                if (!isPageOpen) {
                    slideDesc.setVisibility(View.VISIBLE);
                    slideDesc.startAnimation(translateUpAnim);
                }
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                if (isPageOpen) {
                    slideDesc.startAnimation(translateDownAnim);
                } else {
                    Objects.requireNonNull(getActivity()).finish();
                }
                return true; // Top to bottom
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (isPageOpen) {
                slideDesc.setVisibility(View.GONE);
                slideDesc.startAnimation(translateDownAnim);
            } else {
                slideDesc.setVisibility(View.VISIBLE);
                slideDesc.startAnimation(translateUpAnim);
            }
            super.onLongPress(e);
        }
    }
}

