package com.example.myapplication;

import android.Manifest;
import android.content.ActivityNotFoundException;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                phone.setIcon(R.drawable.user); //default image from phone data

                datas.add(phone);
            }
        }
        assert cursor != null;
        cursor.close();
        return datas;

    }

    private ArrayList<PhoneNum> phonenum = new ArrayList<>();
    private ArrayList<PhoneNum> phonenum2;
    private SearchAdapter adapter2;

    private EditText editSearch;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        InitializePhoneBook();
        jsonParsing(getJsonString());

        if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            List<PhoneNum> phoneBooks = getContacts();
            if (phoneBooks.size() != 0) {
                int size = phoneBooks.size();
                for (int i = 0; i < size; i++) {
                    PhoneNum phone = phoneBooks.get(i);
                    phonenum.add(phone);
                }
            }
        }
    }

    private String getJsonString() {
        String json = "";
        try {
            InputStream is = Objects.requireNonNull(getActivity()).getAssets().open("Phone.json");
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
                int id = Objects.requireNonNull(getContext()).getResources().getIdentifier(s, "drawable", getContext().getPackageName());

                //int drawable = getResources().getDrawable(id,null);

                phonenum.add(new PhoneNum(id, name, num));

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
        //listView.setAdapter(adapter);

        //search tab
        final EditText editSearch = myView.findViewById(R.id.editSearch);
         //copy the data
        phonenum2 = new ArrayList<PhoneNum>();
        phonenum2.addAll(phonenum);
        adapter2 = new SearchAdapter(phonenum, getContext());
        listView.setAdapter(adapter2);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

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

    public void search(String charText) {
        phonenum.clear();
        if (charText.length() == 0) {
            phonenum.addAll(phonenum2);
        }else{
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < phonenum2.size(); i++)
            {
                if (phonenum2.get(i).getUserName().toLowerCase().contains(charText.toLowerCase()))
                {
                    phonenum.add(phonenum2.get(i));
                }
            }
        }
        adapter2.notifyDataSetChanged();
    }

    public void InitializePhoneBook() {
        phonenum.add(new PhoneNum(R.drawable.tangle1, "라푼젤", "01071969761"));
    }
}