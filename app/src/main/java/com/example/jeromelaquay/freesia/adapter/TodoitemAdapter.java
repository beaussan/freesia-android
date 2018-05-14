package com.example.jeromelaquay.freesia.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeromelaquay.freesia.R;
import com.example.jeromelaquay.freesia.TodolistbyidQuery;

import java.util.List;

public class TodoitemAdapter extends RecyclerView.Adapter<TodoitemAdapter.ViewHolder> {

    private List<TodolistbyidQuery.TodoItem> mTodoitemList;

    private static  OnRecyclerItemClickListener mOnRecyclerItemClickListener;

    public TodoitemAdapter(List<TodolistbyidQuery.TodoItem> itemList, OnRecyclerItemClickListener onRecyclerItemClickListener){
        mTodoitemList=itemList;
        mOnRecyclerItemClickListener =onRecyclerItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todoitem, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodolistbyidQuery.TodoItem item = mTodoitemList.get(position);
        holder.text.setText(" "+item.text());
    }

    @Override
    public int getItemCount() {
        if(mTodoitemList != null){
            String siz = Integer.toString(mTodoitemList.size());
            Log.d("TodoitemAdapter", siz);
            return mTodoitemList.size();
        }
        Log.d("TodoitemAdapter", "0");
        return 0;
    }

    public long getTodoitemId(int position){
        return mTodoitemList.get(position).id();
    }

    public TodolistbyidQuery.TodoItem getTodoitem(int position){return this.mTodoitemList.get(position); }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView text;

        public ViewHolder(View v){
            super(v);
            text = v.findViewById(R.id.textTxt);
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