package com.zaelani.mynotesapp;

import android.view.View;

public class CustomOnItemClickListener implements View.OnClickListener {
    private int position;
    private OnItemClickCallback onItemClickCallback;

    public CustomOnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(view, position);
    }


    public interface OnItemClickCallback{
        void onItemClicked(View view, int position);
    }
}
