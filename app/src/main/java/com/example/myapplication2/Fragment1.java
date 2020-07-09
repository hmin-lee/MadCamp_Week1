package com.example.myapplication2;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication2.phoneBook.PhoneBook;
import com.example.myapplication2.phoneBook.PhoneListViewItem;
import com.example.myapplication2.phoneBook.UserInfo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {
    PhoneBook myPhoneBook;

    public Fragment1() {
        // Required empty public constructor
    }

    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitializePhoneBook();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View phoneView = inflater.inflate(R.layout.fragment_1, container, false);

        // getActivity()? getContext()?
        ListViewAdapter adapter = new ListViewAdapter();
        ListView listView = phoneView.findViewById(R.id.listview_phonebook);
        listView.setAdapter(adapter);

        // 아이템 추가
        addItemsInAdapter(adapter);

        // 클릭 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                PhoneListViewItem item = (PhoneListViewItem) parent.getItemAtPosition(position);

                String nameString = item.getNameString();
                String numberString = item.getNumberString();

                System.out.println(">>>>>>" + nameString + " : " + numberString);
            }
        });

        return phoneView;
    }

    public void InitializePhoneBook() {
        myPhoneBook = getPhoneBookFromJson();

    }

    public void addItemsInAdapter(ListViewAdapter adapter) {
        for (UserInfo user : myPhoneBook.getUsers()) {
            System.out.println("AddItemsInAdapter " + user.getName());
            adapter.addItem(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_baseline_account_circle_36), user.getName(), user.getNumber());
        }
    }

    public PhoneBook getPhoneBookFromJson() {
        PhoneBook phoneBook = new PhoneBook();
        Gson gson = new Gson();
        try {
            InputStream inputStream = getActivity().getAssets().open("phones.json");
            byte[] buffer = new byte[inputStream.available()];
            int read = inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("UserInfo");

            int index = 0;
            while (index < jsonArray.length()) {
                UserInfo userInfo = gson.fromJson(jsonArray.get(index).toString(), UserInfo.class);
                phoneBook.addUserInfo(userInfo);

                index++;
            }

        } catch (Exception e) {
            System.out.println(">>getPhoneBookFromJson Error");
            e.printStackTrace();
        }
        return phoneBook;
    }

}