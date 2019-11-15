package com.example.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewAllActivity extends AppCompatActivity {

    private ListView listView2;
    private TextView writeReviews2;
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewall);

        listView2 = (ListView) findViewById(R.id.listView2);
        writeReviews2 = (TextView) findViewById(R.id.writeReviews2);

        adapter = new CommentsAdapter();
        adapter.addItem(new CommentsItem("minjik95", 10));
        adapter.addItem(new CommentsItem("junkuk98", 5));
        adapter.addItem(new CommentsItem("minjik1225", 15));
        adapter.addItem(new CommentsItem("solongmypartner", 20));
        adapter.addItem(new CommentsItem("heyyy", 25));

        listView2.setAdapter(adapter);

        writeReviews2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
