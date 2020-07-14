package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class SearchAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PhoneNum> list;
    private LayoutInflater inflate;
    private UserViewHolder viewHolder;

    public SearchAdapter(ArrayList<PhoneNum> list, Context context) {
        this.list = list;
        this.mContext = context;
        this.inflate = LayoutInflater.from(context);
    }

    public SearchAdapter() {
    }

    public SearchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public PhoneNum getItem(int position) {
        return list.get(position);
    }
//
//    public void addNum(int icon, String name, String number) {
//        PhoneNum item = new PhoneNum(icon, name, number);
//        list.add(item);
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.listview_item, null);

            viewHolder = new UserViewHolder();
            viewHolder.txticon = convertView.findViewById(R.id.imageView1);
            viewHolder.txtname = convertView.findViewById(R.id.textView1);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (UserViewHolder) convertView.getTag();
        }
        if (list.get(position).getIcon() != 0){
            System.out.println(">>>>>>>>>list.get(position).getIcon() != 0");
            Glide.with(mContext).load(list.get(position).getIcon()).into(viewHolder.txticon);
        }else{
            System.out.println(">>>>>>>>>list.get(position).getIcon() == 0: "+list.get(position).getIconUri());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:"+list.get(position).getIcon());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:"+list.get(position).getUserName());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:"+list.get(position).toString());
            Glide.with(mContext).load(list.get(position).getIconUri()).into(viewHolder.txticon);
        }
        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        //viewHolder.txticon.setImageResource(list.get(position).getIcon());
        viewHolder.txtname.setText(list.get(position).getUserName());

        return convertView;
    }

    class UserViewHolder {
        ImageView txticon;
        TextView txtname;
    }

}