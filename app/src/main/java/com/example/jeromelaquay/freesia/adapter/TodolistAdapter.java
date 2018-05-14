package com.example.jeromelaquay.freesia.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeromelaquay.freesia.AllTodoListQuery;
import com.example.jeromelaquay.freesia.R;

import java.util.List;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.ViewHolder> {

    private List<AllTodoListQuery.AllTodoList> mTodolistList;

    private static  OnRecyclerItemClickListener mOnRecyclerItemClickListener;

    public TodolistAdapter(List<AllTodoListQuery.AllTodoList> TodoList, OnRecyclerItemClickListener onRecyclerItemClickListener){
        mTodolistList=TodoList;
        mOnRecyclerItemClickListener =onRecyclerItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todolist, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AllTodoListQuery.AllTodoList todo = mTodolistList.get(position);
        holder.nbreItems.setText(" Number of tasks : " + todo.todoItems().size());
        holder.title.setText("  "+todo.title());
    }

    @Override
    public int getItemCount() {
        if(mTodolistList != null){
            String siz = Integer.toString(mTodolistList.size());
            Log.d("TodolistAdapter", siz);
            return mTodolistList.size();
        }
        Log.d("TodolistAdapter", "0");
        return 0;
    }

    public long getTodolist(int position){
        return this.mTodolistList.get(position).id();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title;
        private TextView nbreItems;

        public ViewHolder(View v){
            super(v);
            title = v.findViewById(R.id.titleTxt);
            nbreItems = v.findViewById(R.id.NbreItemTxt);
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