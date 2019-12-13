package com.example.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {

    private Button save;
    private Button cancel;

    private TextView reviewTitle;
    private ImageView reviewGrade;

    private String id;
    private String title;
    private String grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        title = intent.getExtras().getString("title");
        grade = intent.getExtras().getString("grade");

        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);

        reviewTitle = (TextView) findViewById(R.id.reviewTitle);
        reviewGrade = (ImageView) findViewById(R.id.reviewGrade);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.putExtra("id", id);
                startActivity(mainIntent);
            }
        });

        reviewTitle.setText(title);

        if(grade.equals("12")) reviewGrade.setImageResource(R.drawable.ic_12);
        else if(grade.equals("15")) reviewGrade.setImageResource(R.drawable.ic_15);
        else if(grade.equals("19")) reviewGrade.setImageResource(R.drawable.ic_19);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
