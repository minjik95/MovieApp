package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.RatingBar;
import android.widget.TextView;

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

        TextView commentsWriter = (TextView) view.findViewById(R.id.commentsWriter);
        TextView commentsTime = (TextView) view.findViewById(R.id.commentsTime);
        RatingBar commentsRating = (RatingBar) view.findViewById(R.id.commentsRating);
        TextView commentsContent = (TextView) view.findViewById(R.id.commentsContent);
        TextView commentsRecommend = (TextView) view.findViewById(R.id.commentsRecommend);

        commentsWriter.setText(items.get(i).getWriter());
        commentsTime.setText(items.get(i).getTime());
        commentsRating.setRating(items.get(i).getRating());
        commentsContent.setText(items.get(i).getContent());
        commentsRecommend.setText(items.get(i).getRecommend());

        return view;
    }
}