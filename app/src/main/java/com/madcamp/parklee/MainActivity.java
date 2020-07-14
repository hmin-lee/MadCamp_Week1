package com.madcamp.parklee;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSelfPermission();

        ViewPager vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(vp);

        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.icon_phone);
        images.add(R.drawable.icon_gallery);
        images.add(R.drawable.icon_diary);


        for(int i=0; i<3; i++){
            tab.getTabAt(i).setIcon(images.get(i));
        }

        Objects.requireNonNull(tab.getTabAt(0).getIcon()).setColorFilter(Color.parseColor("#8E4DEA"), PorterDuff.Mode.SRC_ATOP);
        Objects.requireNonNull(tab.getTabAt(1).getIcon()).setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_ATOP);
        Objects.requireNonNull(tab.getTabAt(2).getIcon()).setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_ATOP);

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#8E4DEA"), PorterDuff.Mode.SRC_ATOP);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_ATOP);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//권한을 허용 했을 경우
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {// 동의
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
                }
            }

        }
    }

    public void checkSelfPermission() {
        String tmp = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            tmp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            tmp += Manifest.permission.WRITE_CONTACTS + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            tmp += Manifest.permission.READ_CONTACTS + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            tmp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            tmp += Manifest.permission.CAMERA + " ";
        }

        if (!TextUtils.isEmpty(tmp)) { // 권한 요청
            ActivityCompat.requestPermissions(this, tmp.trim().split(" "), 1);
        } else { // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }


    }
}