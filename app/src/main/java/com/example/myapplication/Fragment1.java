package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//                // get item
//                PhoneNum item = (PhoneNum) parent.getItemAtPosition(position);
//
//                String num = item.getNum();
//                String userName = item.getUserName();
//            }
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

                Drawable drawable = getResources().getDrawable(id,null);

                System.out.println(drawable.toString());
                phonenum.addNum(drawable, name, num);

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
        PhoneAdapter adapter = phonenum;
//        PhoneAdapter adapter = new PhoneAdapter();
//        for (int i = 0; i < phonenum.getCount(); i++){
//            PhoneNum user = phonenum.getItem(i);
//            adapter.addNum(user.getUserName(), user.getNum());
        listView.setAdapter(adapter);
//        }
        return myView;
    }

    public void InitializePhoneBook() {
        phonenum.addNum(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.baseline_account_box_black_18dp),
                "이혜민", "01071969761");
        //this가 main에서만 쓰여서 Objects.requireNonNull(getContext())
        phonenum.addNum(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.baseline_account_circle_black_18dp),
                "이혜만", "01097617196");
    }
}