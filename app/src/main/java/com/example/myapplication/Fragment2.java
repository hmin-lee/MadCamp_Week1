package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Fragment2 extends Fragment implements GalleryRecyclerAdapter.OnGalleryListener {
    private static final String TAG = "Fragment2_1";
    public static ArrayList< String> mImages;
    Context myContext;

    // For Using Camera Activity
    static final int REQUEST_IMAGE_CAPTURE = 101;

    public Fragment2() {
        // Required empty public constructor
    }

    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<String> getImagesFromStorage() {
        ArrayList<String> res = new ArrayList<>();
        try {
            File imagePath = new File(myContext.getFilesDir(), "images");
            for (File s : imagePath.listFiles()) {
                Log.d(TAG, "onClick: string:" + s.getPath());
                if (new File(s.getPath()).exists()) {
                    Log.d(TAG, "onClick: EXISTS!!");
                    res.add(s.getPath());
                } else
                    Log.d(TAG, "onClick: NOT_EXISTS!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
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
        myContext = getContext();
        mImages = getImagesFromStorage();

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
    public void onGalleryClick(int position, ArrayList<String> mImages) {
        Intent intent = new Intent(myContext, ImageDetailActivity.class);
        intent.putExtra("id", position);
        intent.putExtra("images",  mImages);
        startActivity(intent);
    }

    class FABClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "FAB 클릭!! 구현 안함...ㅎ", Toast.LENGTH_SHORT).show();
            try {
//                dispatchTakePictureIntent();
                File imagePath = new File(myContext.getFilesDir(), "images");
//                Log.d(TAG, "onClick: FAB 클릭"+file.toString());
                for (File s : imagePath.listFiles()) {
                    Log.d(TAG, "onClick: string:" + s.getPath());
                    if (new File(s.getPath()).exists())
                        Log.d(TAG, "onClick: EXISTS!!");
                    else
                        Log.d(TAG, "onClick: NOT_EXISTS!!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an Image File name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = myContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 111;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(myContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "dispatchTakePictureIntent: Error!!!");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(myContext,
                        "com.example.myapplication.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    if (bitmap != null) {
                        Toast.makeText(myContext, "onActivityResult: 이미지 파일 가져왔음", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }
}