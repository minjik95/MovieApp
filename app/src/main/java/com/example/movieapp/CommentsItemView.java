package com.example.movieapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentsItemView extends LinearLayout {
    private TextView commentsWriter;
    private TextView commentsTime;

    public CommentsItemView(Context context) {
        super(context);

        init(context);
    }

    public CommentsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comments_item, this, true);

        commentsWriter = (TextView) findViewById(R.id.commentsWriter);
        commentsTime = (TextView) findViewById(R.id.commentsTime);

    }

    public void setId(String id) {
        commentsWriter.setText(id);
    }

    public void setTime(int time) {
        commentsTime.setText(String.valueOf(time));
    }
}
