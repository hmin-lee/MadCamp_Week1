package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<PhoneNum> getContacts(){
        ArrayList<PhoneNum> datas = new ArrayList<>();
        ContentResolver resolver = getContext().getContentResolver();
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);
        if (cursor != null){
            while(cursor.moveToNext()){
                int nameIndex = cursor.getColumnIndex(projection[0]);
                int numberIndex = cursor.getColumnIndex(projection[1]);

                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);

                PhoneNum phone = new PhoneNum(name,number);
                phone.setIcon(R.drawable.baseline_account_box_black_18dp); //default image from phone data

                datas.add(phone);
            }
        }
        assert cursor != null;
        cursor.close();
        return datas;

    }

    PhoneAdapter phonenum = new PhoneAdapter();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        InitializePhoneBook();
        jsonParsing(getJsonString());

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>"+phonenum.getCount());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            List<PhoneNum> phoneBooks = getContacts();
            System.out.println(phoneBooks.size());
            if (phoneBooks.size() != 0) {
                int size = phoneBooks.size();
                for (int i = 0; i < size; i++) {
                    PhoneNum phone = phoneBooks.get(i);
                    phonenum.addNum(phone.getIcon(), phone.getUserName(), phone.getNum());
                }
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>"+phonenum.getCount());
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
        phonenum.addNum(R.drawable.tangled, "라푼젤", "01071969761");
    }
}