package com.example.myapplication;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class ImageDetailFragment extends Fragment {

    private Integer CUR_IMG = 0;

    public Integer[] thumbImages = {
            R.drawable.img1, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8, R.drawable.img9,
            R.drawable.img10, R.drawable.img11, R.drawable.img12,
            R.drawable.img13
    };

    LinearLayout slideDesc;

    GestureDetector gestureDetector;
    Context myContext;
    boolean isPageOpen = false;


    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    Animation translateUpAnim;
    Animation translateDownAnim;

    public ImageDetailFragment(int position) {
        CUR_IMG = position;
//        System.out.println("<<<<<<Fragment 생성자에서 넘겨줌: " + CUR_IMG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CUR_IMG = getArguments().getInt("id");
//            System.out.println("<<<<<<<<<<OnCreate에서 getArg 존재 OO" + CUR_IMG);
        }
//        else {
//            System.out.println("<<<<<<<<<<OnCreate에서 getArg 존재 XX" + CUR_IMG);
//
//        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.image_detail_fragment, container, false);
        myContext = getContext();
        ImageView imageView = myView.findViewById(R.id.image_detail_fragment);

        imageView.setImageResource(thumbImages[CUR_IMG]);

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
                if (touchEvent && isPageOpen) {
                    Toast.makeText(myContext, "플링함", Toast.LENGTH_SHORT).show();
                    slideDesc.startAnimation(translateDownAnim);
                } else if (touchEvent) {
                    Toast.makeText(myContext, "플링함", Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).finish();
                } else {
                    //TODO: 더블탭으로 바꿔보기
                    if (isPageOpen) {
                        slideDesc.setVisibility(View.GONE);
                        slideDesc.startAnimation(translateDownAnim);
                    } else {
                        slideDesc.setVisibility(View.VISIBLE);
                        slideDesc.startAnimation(translateUpAnim);
                    }
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
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return true; // Top to bottom
            }
            return false;
        }
    }
}

