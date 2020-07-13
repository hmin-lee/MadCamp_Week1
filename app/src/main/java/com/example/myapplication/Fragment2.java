package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class Fragment2 extends Fragment implements GalleryRecyclerAdapter.OnGalleryListener {
    private static final String TAG = "Fragment2_1";
    public static ArrayList<Integer> mImages;
    Context myContext;

    public Fragment2() {
        // Required empty public constructor
    }

    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImages = new ArrayList<>();
        Integer[] tmp = {
                R.drawable.img1, R.drawable.img2, R.drawable.img3,
                R.drawable.img4, R.drawable.img5, R.drawable.img6,
                R.drawable.img7, R.drawable.img8, R.drawable.img9,
                R.drawable.img10, R.drawable.img11, R.drawable.img12,
                R.drawable.img13, R.drawable.img1, R.drawable.img2, R.drawable.img3,
                R.drawable.img4, R.drawable.img5, R.drawable.img6,
                R.drawable.img7, R.drawable.img8, R.drawable.img9,
        };
        Collections.addAll(mImages, tmp);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_2, container, false);
        myContext = getContext();

        initRecyclerView(myView);


        FloatingActionButton fab = myView.findViewById(R.id.image_fab);
        fab.setOnClickListener(new FABClickListener());

        return myView;
    }

    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init RecyclerView");
        int numOfColumns = 3;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        GalleryRecyclerAdapter galleryRecyclerAdapter = new GalleryRecyclerAdapter(myContext, mImages, this);

        recyclerView.setAdapter(galleryRecyclerAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(myContext, numOfColumns));

    }

    @Override
    public void onGalleryClick(int position) {
        Intent intent = new Intent(myContext, ImageDetailActivity.class);
        intent.putExtra("id", position);
        startActivity(intent);
    }

    class FABClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "FAB 클릭!! 구현 안함...ㅎ", Toast.LENGTH_SHORT).show();
        }
    }


}