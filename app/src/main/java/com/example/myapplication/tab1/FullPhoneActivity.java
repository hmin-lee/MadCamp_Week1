package com.example.myapplication.tab1;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.Objects;

public class FullPhoneActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_details);

        Intent intent = getIntent();

        final String num = Objects.requireNonNull(intent.getExtras()).getString("phone_num");
        String userName = Objects.requireNonNull(intent.getExtras()).getString("user_name");
        String uri = Objects.requireNonNull(intent.getExtras()).getString("uri");
        int icon = Objects.requireNonNull(intent.getExtras()).getInt("icon");

        ImageView iconImageView = findViewById(R.id.imageView_detail);
        TextView nameTextView = findViewById(R.id.textView_detail1);
        TextView numTextView = findViewById(R.id.textView_detail2);

        if (icon != 0){
            iconImageView.setImageResource(icon);
        }else{
            Glide.with(this).load(uri).into(iconImageView);
        }
        //iconImageView.setImageResource(icon);
        nameTextView.setText(userName);
        numTextView.setText(num);

        ImageButton btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            String callnum = "tel:" + num;

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse(callnum));
                try {
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Intent callIntent = new Intent(Intent.ACTION_VIEW);
                    callIntent.setData(Uri.parse(callnum));
                    if (callIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(callIntent);
                    }
                }
            }
        });

        ImageButton btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            String smsnum = "sms:" + num;

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse(smsnum));
                try {
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse(smsnum));
                    if (smsIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(smsIntent);
                    }
                }
            }
        });


    }
}


