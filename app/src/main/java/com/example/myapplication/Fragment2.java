package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Fragment2 extends Fragment implements GalleryRecyclerAdapter.OnGalleryListener {
    private static final String TAG = "Fragment2_1";
    public static ArrayList< String> mImages;
    Context myContext;
    File previousPhotoFile;

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
            try {
                dispatchTakePictureIntent();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an Image File name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File imagePath = new File(myContext.getFilesDir(), "images");
        if(!imagePath.exists()){
            Log.d(TAG, "getImagesFromStorage: Image Folder doesn't exist. Create one.");
            imagePath.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                imagePath      /* directory */
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
            previousPhotoFile = null;
            try {
                previousPhotoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "dispatchTakePictureIntent: Error!!!");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (previousPhotoFile != null) {
                Log.d(TAG, "dispatchTakePictureIntent: photoFile:"+ previousPhotoFile.toString());
                Uri photoURI = FileProvider.getUriForFile(myContext,
                        "com.example.myapplication.fileprovider",
                        previousPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            Toast.makeText(myContext, "파일["+previousPhotoFile.toString()+"]을 잘 가져왔습니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onActivityResult: previousPhotoFile: "+previousPhotoFile.toString());


            // Refresh this Fragment
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}