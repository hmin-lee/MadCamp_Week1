package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {
    static final int RESULT_LOAD_IMAGE = 101;
    Uri uri;

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_2, container, false);
        final Context myContext = getContext();
        GridView gridView = myView.findViewById(R.id.gridview);
        gridView.setAdapter(new GalleryAdapter(myContext));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(myContext, "Image Position:" + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(myContext, ImageDetailActivity.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });

        FloatingActionButton fab = myView.findViewById(R.id.image_fab);
        fab.setOnClickListener(new FABClickListener());

        return myView;
    }

    class FABClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "FAB 클릭!", Toast.LENGTH_SHORT).show();
        }
    }


}