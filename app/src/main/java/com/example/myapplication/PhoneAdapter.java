package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PhoneAdapter extends BaseAdapter {
    private ArrayList<PhoneNum> PhoneList = new ArrayList<>() ;
    public PhoneAdapter() {
    }
    @Override
    public int getCount() {
        return PhoneList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public PhoneNum getItem(int position) {
        return PhoneList.get(position);
    }

    public void addNum(Drawable icon, String name, String number) {
        PhoneNum item = new PhoneNum(icon, name, number);
        PhoneList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        ImageView iconImageView = convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView =  convertView.findViewById(R.id.textView1) ;
        TextView descTextView =  convertView.findViewById(R.id.textView2) ;

        PhoneNum Phone = PhoneList.get(position);

        iconImageView.setImageDrawable(Phone.getIcon());
        titleTextView.setText(Phone.getUserName());
        descTextView.setText(Phone.getNum());

        return convertView;
    }

}
