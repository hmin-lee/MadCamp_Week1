package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.Inflater;

public class Fragment1 extends Fragment {
    public Fragment1() {
        // Required empty public constructor
    }

    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    PhoneAdapter phonenum = new PhoneAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        InitializePhoneBook();
        jsonParsing(getJsonString());
    }

    private String getJsonString() {
        String json = "";
        try {
            InputStream is = getActivity().getAssets().open("Phone.json");
            int fileSize = is.available();
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    private void jsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray phoneArray = jsonObject.getJSONArray("Phones");

            for (int i = 0; i < phoneArray.length(); i++) {
                JSONObject phoneObject = phoneArray.getJSONObject(i);

                String icon = phoneObject.getString("iconDrawable");
                String name = phoneObject.getString("userName");
                String num = phoneObject.getString("num");

                String s = icon.replace("R.drawable.", "");
                int id = getContext().getResources().getIdentifier(s, "drawable", getContext().getPackageName());

                //int drawable = getResources().getDrawable(id,null);

                phonenum.addNum(id, name, num);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_1, container, false);
//    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_1, container, false);
        ListView listView = myView.findViewById(R.id.listView);
        final Context phoneContext = getContext();
        PhoneAdapter adapter = phonenum;
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                PhoneNum item = (PhoneNum) parent.getItemAtPosition(position);

                String num = item.getNum();
                String userName = item.getUserName();
                int icon = item.getIcon();

//                Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();

                Intent i = new Intent(phoneContext, FullPhoneActivity.class);
                i.putExtra("phone_num", num);
                i.putExtra("user_name", userName);
                i.putExtra("icon", icon);
                startActivity(i);

            }
        });
        return myView;
    }

    public void InitializePhoneBook() {
        phonenum.addNum( R.drawable.baseline_account_box_black_18dp, "이혜민", "01071969761");
        //this가 main에서만 쓰여서 Objects.requireNonNull(getContext())
//        phonenum.addNum(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.baseline_account_circle_black_18dp),
//                "이혜만", "01097617196");
    }
}