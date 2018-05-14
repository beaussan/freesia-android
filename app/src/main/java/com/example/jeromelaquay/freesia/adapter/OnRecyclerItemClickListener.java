package com.example.jeromelaquay.freesia.adapter;

import android.view.View;

public interface OnRecyclerItemClickListener {
    void onClick(View view, int position, boolean isLongClick);

    void onLongClick(View view, int position, boolean isLongClick);
}
