package com.example.myapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Objects;

public class FullPhoneActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_details);

        Intent intent = getIntent();

        final String num =  Objects.requireNonNull(intent.getExtras()).getString("phone_num");
        String userName =  Objects.requireNonNull(intent.getExtras()).getString("user_name");
        int icon =  Objects.requireNonNull(intent.getExtras()).getInt("icon");

        //byte[] arr = getIntent().getByteArrayExtra("icon");
//        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
//        ImageView iconImageView = findViewById(R.id.imageView_detail);
//        iconImageView.setImageBitmap(image);


        ImageView iconImageView = findViewById(R.id.imageView_detail) ;
        TextView nameTextView =  findViewById(R.id.textView_detail1) ;
        TextView numTextView =  findViewById(R.id.textView_detail2) ;


        iconImageView.setImageResource(icon);
        nameTextView.setText(userName);
        numTextView.setText(num);


        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            String callnum = "tel:" + num;
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse(callnum)) ;
                try{
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Intent callIntent = new Intent(Intent.ACTION_VIEW);
                    callIntent.setData(Uri.parse(callnum));
                    if (callIntent.resolveActivity(getPackageManager())!=null){
                        startActivity(callIntent);
                    }
                }
            }
        });

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            String smsnum = "sms:" + num;
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse(smsnum)) ;
                try{
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse(smsnum));
                    if (smsIntent.resolveActivity(getPackageManager())!=null){
                        startActivity(smsIntent);
                    }
                }
            }
        });


    }
}


