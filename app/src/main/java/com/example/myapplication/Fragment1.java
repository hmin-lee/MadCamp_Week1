package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Fragment1 extends Fragment {
    public Fragment1() {
        // Required empty public constructor
    }

    public static PhoneDBHelper phoneDBHelper;
    public static SQLiteDatabase db;

    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ArrayList<PhoneNum> getContacts() {
        ArrayList<PhoneNum> datas = new ArrayList<>();
        ContentResolver resolver = getContext().getContentResolver();
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndex(projection[0]);
                int numberIndex = cursor.getColumnIndex(projection[1]);

                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);

                PhoneNum phone = new PhoneNum(name, number);
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
    private ArrayList<PhoneNum> phonebook;
    private SearchAdapter adapter2;

    private EditText editSearch;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phoneDBHelper = new PhoneDBHelper(getContext());
        db = phoneDBHelper.getReadableDatabase();
        phoneDBHelper.onUpgrade(db, 1, 1);
        db.close();
//        InitializePhoneBook();
        jsonParsing(getJsonString());

        // smartphone data
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
                    InsertPhoneBook(phone.getIcon(), phone.getUserName(), phone.getNum());
                }
            }
        }
        phonebook = showPhoneBook();
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
                InsertPhoneBook(id, name, num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_1, container, false);
//    }
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.fragment_1, container, false);
        ListView listView = myView.findViewById(R.id.listView);
        final Context phoneContext = getContext();
        //listView.setAdapter(adapter);

        //search tab
        final EditText editSearch = myView.findViewById(R.id.editSearch);
        //copy the data
        phonenum2 = new ArrayList<PhoneNum>();
//        phonenum2.addAll(phonenum);
//        adapter2 = new SearchAdapter(phonenum, getContext());

        phonenum2.addAll(phonebook);
        adapter2 = new SearchAdapter(phonebook, getContext());
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long l) {
                PhoneNum item = (PhoneNum) parent.getItemAtPosition(position);

                String num = item.getNum();
                final String userName = item.getUserName();
                int icon = item.getIcon();

                phonebook.remove(item);
                DeletePhoneBook(icon, userName, num);
                adapter2.notifyDataSetChanged();

                return true;
            }
        });

        FloatingActionButton phone_fab = myView.findViewById(R.id.phone_fab);


        class PhoneFABClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                View addphoneView = inflater.inflate(R.layout.add_phonenum, container, false);
                alertDialog.setView(addphoneView);
                final EditText nameText = addphoneView.findViewById(R.id.add_username);
                final EditText numText = addphoneView.findViewById(R.id.add_num);
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = nameText.getText().toString();
                        String num = numText.getText().toString();
                        phonenum.add(new PhoneNum(R.drawable.user, name, num));
                        InsertPhoneBook(R.drawable.user, name, num);
                        phonebook.add(new PhoneNum(R.drawable.user, name, num));
                        adapter2.notifyDataSetChanged();

                    }
                });
                alertDialog.show();
            }
        }
        phone_fab.setOnClickListener(new PhoneFABClickListener());
        return myView;
    }

    public void search(String charText) {
        phonebook.clear();
//        phonenum.clear();
        if (charText.length() == 0) {
            phonebook.addAll(phonenum2);
            //phonenum.addAll(phonenum2);
        } else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < phonenum2.size(); i++) {
                if (phonenum2.get(i).getUserName().toLowerCase().contains(charText.toLowerCase())) {
                    phonebook.add(phonenum2.get(i));
                }
            }
        }
        adapter2.notifyDataSetChanged();
    }

    public void InitializePhoneBook() {
        phonenum.add(new PhoneNum(R.drawable.tangle1, "라푼젤", "01071969761"));
    }

    public static boolean isExistPhoneBook(String name) {
        db = phoneDBHelper.getReadableDatabase();
        String sql = "SELECT * FROM phonebook WHERE name='" + name + "';";
        Cursor cursor = db.rawQuery(sql, null);
        boolean res = false;
        while (cursor.moveToNext()) {
            res = true;
            Log.d(TAG, "isExistDiary: 다이어리:" + name + ", " + cursor.getString(2));
        }
        cursor.close();
        db.close();
        return res;
    }


    public static void InsertPhoneBook(int icon, String name, String num) {
        db = phoneDBHelper.getWritableDatabase();
        String sql = "insert into PhoneBook('icon', 'name', 'num') values(?,?,?);";
        SQLiteStatement st = db.compileStatement(sql);
        st.bindString(1, "" + icon);
        st.bindString(2, name);
        st.bindString(3, num);
        st.execute();
        db.close();
    }

//    public static void UpdatePhoneBook(int icon, String name, String num) {
//        if (isExistPhoneBook(name)) {
//            db = phoneDBHelper.getWritableDatabase();
//            String sql = "UPDATE PhoneBook SET icon=?, num=? WHERE name=?";
//            SQLiteStatement st = db.compileStatement(sql);
//            st.bindString(1, ""+icon);
//            st.bindString(2, name);
//            st.bindString(3, num);
//            st.execute();
//            db.close();
//        } else {
//            Log.d(TAG, "UpdateDiary: 다이어리 존재하지 않음");
//        }
//    }

    public static void DeletePhoneBook(int icon, String name1, String num1) {
        db = phoneDBHelper.getWritableDatabase();
        String sql = "delete from PhoneBook where num = ? and name = ?;";
        SQLiteStatement st = db.compileStatement(sql);
        st.bindString(1, num1);
        st.bindString(2, name1);
        st.execute();
        db.close();
    }


    public static ArrayList<PhoneNum> showPhoneBook() {
        db = phoneDBHelper.getReadableDatabase();
        ArrayList<PhoneNum> res = new ArrayList<>();

        String sql = "select * from phonebook";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String icon = cursor.getString(1); // date
            String name = cursor.getString(2);
            String num = cursor.getString(3);// content
            //Log.d(TAG, "showDiary: Show Diary. [Data]: date: " + name + ", content: " + content);
            res.add(new PhoneNum(Integer.parseInt(icon), name, num));
        }

        cursor.close();
        db.close();
        return res;
    }

}