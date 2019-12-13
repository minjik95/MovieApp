package com.example.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewAllActivity extends AppCompatActivity {
    private RequestQueue requestQueue;

    private String id;
    private String title;
    private String grade;
    private float user_rating;

    private TextView commentsAllTitle;
    private ImageView commentsAllGrade;
    private RatingBar commentsAllRatingBar;
    private TextView commentsRatingNum;
    private TextView commentsTotal;

    private ListView listView2;
    private TextView writeReviews2;
    private CommentsAdapter adapter;

    private int count;
    private ArrayList<String> commentsId = new ArrayList<>();
    private ArrayList<String> commentsWriter = new ArrayList<>();
    private ArrayList<String> commentsTime = new ArrayList<>();
    private ArrayList<Float> commentsRating = new ArrayList<>();
    private ArrayList<String> commentsContent = new ArrayList<>();
    private ArrayList<String> commentsRecommend = new ArrayList<>();

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewall);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        title = intent.getExtras().getString("title");
        grade = intent.getExtras().getString("grade");
        user_rating = intent.getExtras().getFloat("user_rating");

        if(requestQueue == null) requestQueue = Volley.newRequestQueue(getApplicationContext());
        sendRequest();

        commentsAllTitle = (TextView) findViewById(R.id.commentsAllTitle);
        commentsAllGrade = (ImageView) findViewById(R.id.commentsAllGrade);
        commentsAllRatingBar = (RatingBar) findViewById(R.id.commentsAllRatingBar);
        commentsRatingNum = (TextView) findViewById(R.id.commentsRatingNum);
        commentsTotal = (TextView) findViewById(R.id.commentsTotal);

        listView2 = (ListView) findViewById(R.id.listView2);
        writeReviews2 = (TextView) findViewById(R.id.writeReviews2);

        commentsAllTitle.setText(title);
        if(grade.equals("12")) commentsAllGrade.setImageResource(R.drawable.ic_12);
        else if(grade.equals("15")) commentsAllGrade.setImageResource(R.drawable.ic_15);
        else if(grade.equals("19")) commentsAllGrade.setImageResource(R.drawable.ic_19);
        commentsAllRatingBar.setRating(user_rating);
        commentsRatingNum.setText(String.valueOf(user_rating));

        adapter = new CommentsAdapter();

        writeReviews2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewIntent = new Intent(getApplicationContext(), ReviewActivity.class);
                reviewIntent.putExtra("id", id);
                reviewIntent.putExtra("title", title);
                reviewIntent.putExtra("grade", grade);
                startActivity(reviewIntent);
            }
        });

    }

    public void sendRequest() {
        String commentUrl = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=" + id;

        StringRequest commentRequest = new StringRequest(
                Request.Method.GET,
                commentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processCommentResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", "" + error);
                    }
                }
        );

        commentRequest.setShouldCache(false);
        requestQueue.add(commentRequest);
    }

    public void processCommentResponse(String response) {
        Gson gson = new Gson();
        ReadCommentList readCommentList = gson.fromJson(response, ReadCommentList.class);
        count = readCommentList.result.size();

        if(readCommentList != null) {
            for(int i = 0; i < count; i++) {
                commentsId.add(readCommentList.result.get(i).id);
                commentsWriter.add(readCommentList.result.get(i).writer);
                commentsTime.add(readCommentList.result.get(i).time);
                commentsRating.add(readCommentList.result.get(i).rating);
                commentsContent.add(readCommentList.result.get(i).contents);
                commentsRecommend.add(readCommentList.result.get(i).recommend);
            }
        }

        commentsTotal.setText(String.valueOf(count));

        addItemsToAdapter();
    }

    public void addItemsToAdapter() {
        for(int i = 0; i < count; i++) {
            String time = calculateTime(commentsTime.get(i));
            adapter.addItem(new CommentsItem(commentsId.get(i), commentsWriter.get(i), time, commentsRating.get(i), commentsContent.get(i), commentsRecommend.get(i)));
        }

        listView2.setAdapter(adapter);
    }

    public String calculateTime(String time) {
        String regexTime = time.replaceAll("[^0-9]", "");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        long curTime = System.currentTimeMillis();
        long parsedRegexTime = 0L;
        long diffTime = 0L;
        Date d = null;

        try {
            d = dateFormat.parse(regexTime);
            parsedRegexTime = d.getTime();
            diffTime = (curTime - parsedRegexTime) / 1000;

        } catch (Exception e) {
            e.printStackTrace();
        }

        String result;

        if(diffTime < SEC) result = diffTime + "초전";
        else if((diffTime /= SEC) < MIN) result = diffTime + "분전";
        else if((diffTime /= MIN) < HOUR) result = diffTime + "시간전";
        else if((diffTime /= HOUR) < DAY) result = diffTime + "일전";
        else if((diffTime /= DAY) < MONTH) result = diffTime + "달전";
        else result = diffTime + "년전";

        return result;
    }

}
