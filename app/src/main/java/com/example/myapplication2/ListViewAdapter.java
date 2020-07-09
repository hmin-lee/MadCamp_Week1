package com.example.myapplication2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication2.phoneBook.PhoneListViewItem;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<PhoneListViewItem> listViewItems = new ArrayList<>();

    public ListViewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        final int pos = position;
        final Context context = parentView.getContext();

        // listview_item Layout을 inflate하여 convertView참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parentView, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView1);
        TextView nameTextView = convertView.findViewById(R.id.textView1);
        TextView numberTextView = convertView.findViewById(R.id.textView2);

        //Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        PhoneListViewItem listViewItem = listViewItems.get(pos);

        // 아이템 내 각 위젯에 데이터 반영
        imageView.setImageDrawable(listViewItem.getIconDrawable());
        nameTextView.setText(listViewItem.getNameString());
        numberTextView.setText(listViewItem.getNumberString());

        return convertView;
    }

    public void addItem(Drawable icon, String name, String number) {
        PhoneListViewItem item = new PhoneListViewItem();

        item.setIconDrawable(icon);
        item.setNameString(name);
        item.setNumberString(number);

        listViewItems.add(item);
    }
}
