package com.example.jeromelaquay.freesia.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeromelaquay.freesia.AllTodoListQuery;
import com.example.jeromelaquay.freesia.Photo;
import com.example.jeromelaquay.freesia.R;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<Photo> mPhotos;

    private static  OnRecyclerItemClickListener mOnRecyclerItemClickListener;

    public PhotoAdapter(List<Photo> photos, OnRecyclerItemClickListener onRecyclerItemClickListener){
        mPhotos=photos;
        mOnRecyclerItemClickListener =onRecyclerItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = mPhotos.get(position);
        byte [] encodeByte=Base64.decode(photo.getImage(), Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        holder.image.setImageBitmap(bitmap);
        holder.message.setText(photo.getMessage());
    }

    @Override
    public int getItemCount() {
        if(mPhotos != null){
            String siz = Integer.toString(mPhotos.size());
            Log.d("PhotoAdapter", siz);
            return mPhotos.size();
        }
        Log.d("PhotoAdapter", "0");
        return 0;
    }

    public Photo getTodolist(int position){
        return this.mPhotos.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView image;
        private TextView message;

        public ViewHolder(View v){
            super(v);
            image = v.findViewById(R.id.imagePhoto);
            message = v.findViewById(R.id.messagePhoto);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnRecyclerItemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnRecyclerItemClickListener.onLongClick(v, getAdapterPosition(), false);
            return true;
        }
    }
}