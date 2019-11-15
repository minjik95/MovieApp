package com.example.movieapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

class CommentsAdapter extends BaseAdapter {
    ArrayList<CommentsItem> items = new ArrayList<>();
    private LayoutInflater inflater;

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(CommentsItem item) {
        items.add(item);
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            final Context context = viewGroup.getContext();
            if(inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            view = inflater.inflate(R.layout.comments_item, viewGroup, false);
        }

        TextView commentsId = (TextView) view.findViewById(R.id.commentsId);
        TextView commentsTime = (TextView) view.findViewById(R.id.commentsTime);

        commentsId.setText(items.get(i).getId());
        commentsTime.setText(String.valueOf(items.get(i).getTime()));

        Log.d("TAG", "" + items.get(i).getId() + ", " + items.get(i).getTime());

        return view;
//            CommentsItemView commentsItemView = new CommentsItemView(getApplicationContext());
//            CommentsItem item = items.get(i);
//
//            commentsItemView.setId(item.getId());
//            commentsItemView.setTime(item.getTime());
//
//            return commentsItemView;
    }
}