package com.madcamp.parklee.tab2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.madcamp.parklee.R;

import java.util.ArrayList;

/**
 * Created by jongwow on 2020-07-13.
 */
public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder> {
    private static final String TAG = "GalleryRecyclerAdapter";

    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;
    private OnGalleryListener mOnGalleryListener;


    public GalleryRecyclerAdapter(Context context, ArrayList<String> mImages, OnGalleryListener onGalleryListener) {
        this.mContext = context;
        this.mImages = mImages;
        this.mOnGalleryListener = onGalleryListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_listitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnGalleryListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // Important
        Log.d(TAG, "onBindViewHolder: 새롭게 생성될 경우");

        ImageView imageView = holder.imageView;
        String resourceId = mImages.get(position);

        Glide.with(mContext).load(resourceId).into(imageView);

    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    // for clicking on an item
    public interface OnGalleryListener {
        void onGalleryClick(int position, ArrayList<String> images);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // 위젯을 메모리에 넣음.
        ImageView imageView;
        RelativeLayout parent_layout;
        OnGalleryListener onGalleryListener;

        public ViewHolder(@NonNull View itemView, OnGalleryListener onGalleryListener) {
            super(itemView);
            Log.d(TAG, "ViewHolder: 생성된:" + itemView.toString());
            imageView = itemView.findViewById(R.id.gallery_item);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            this.onGalleryListener = onGalleryListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onGalleryListener.onGalleryClick(getAdapterPosition(), mImages);
        }
    }
}
