package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RequestQueue requestQueue;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ListView listView;
    private CommentsAdapter commentsAdapter;

    private ImageView movieMainImg;
    private ImageView movieAgeRating;

    private TextView movieTitle;
    private TextView movieReleaseDate;
    private TextView movieGenre;
    private TextView movieDuration;
    private TextView thumbUpNum;
    private TextView thumbDownNum;
    private TextView movieReservationGrade;
    private TextView movieReservationRate;
    private TextView movieAudienceRating;
    private TextView movieAudience;
    private TextView movieSynopsis;
    private TextView movieDirector;
    private TextView movieActor;
    private TextView writeReviews;

    private Button thumbUp;
    private Button thumbDown;
    private Button viewAllComments;

    private RecyclerView recyclerView;

    private int thumbUpNumber = 0;
    private int thumbDownNumber = 0;
    private boolean thumbUpCheck = false;
    private boolean thumbDownCheck = false;

    private String title;
    private String id;
    private String date;
    private float user_rating;
    private String audience_rating;
    private String reservation_rate;
    private String reservation_grade;
    private String grade;
    private String thumb;
    private String photos;
    private String videos;
    private String[] array_photos;
    private String[] array_videos;
    private String genre;
    private String duration;
    private String audience;
    private String synopsis;
    private String director;
    private String actor;
    private String like;
    private String dislike;

    private MovieGalleryAdapter movieGalleryAdapter;

    private ArrayList<String> commentsId = new ArrayList<>();
    private ArrayList<String> commentsWriter = new ArrayList<>();
    private ArrayList<String> commentsTime = new ArrayList<>();
    private ArrayList<Float> commentsRating = new ArrayList<>();
    private ArrayList<String> commentsContent = new ArrayList<>();
    private ArrayList<String> commentsRecommend = new ArrayList<>();
    private int count;

    private static HashMap<String, Bitmap> bitmapHashMap = new HashMap<>();

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            id = intent.getExtras().getString("id");
            Log.d("result: ", "" + id);
        }

        if(requestQueue == null) requestQueue = Volley.newRequestQueue(getApplicationContext());
        sendRequest();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_detail);
        navigationView = (NavigationView) findViewById(R.id.nav_view_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        listView = (ListView) findViewById(R.id.listView);

        movieMainImg = (ImageView) findViewById(R.id.movieMainImg);
        movieAgeRating = (ImageView) findViewById(R.id.movieAgeRating);

        movieTitle = (TextView) findViewById(R.id.movieTitle);
        movieReleaseDate = (TextView) findViewById(R.id.movieReleaseDate);
        movieGenre = (TextView) findViewById(R.id.movieGenre);
        movieDuration = (TextView) findViewById(R.id.movieDuration);
        thumbUpNum = (TextView) findViewById(R.id.thumbUpNum);
        thumbDownNum = (TextView) findViewById(R.id.thumbDownNum);
        movieReservationGrade = (TextView) findViewById(R.id.movieReservationGrade);
        movieReservationRate = (TextView) findViewById(R.id.movieReservationRate);
        movieAudienceRating = (TextView) findViewById(R.id.movieAudienceRating);
        movieAudience = (TextView) findViewById(R.id.movieAudience);
        movieSynopsis = (TextView) findViewById(R.id.movieSynopsis);
        movieDirector = (TextView) findViewById(R.id.movieDirector);
        movieActor = (TextView) findViewById(R.id.movieActor);
        writeReviews = (TextView) findViewById(R.id.writeReviews);

        thumbUp = (Button) findViewById(R.id.thumbUp);
        thumbDown = (Button) findViewById(R.id.thumbDown);
        viewAllComments = (Button) findViewById(R.id.viewAllComments);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("영화 상세");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);

        navigationView.setNavigationItemSelectedListener(this);

        commentsAdapter = new CommentsAdapter();

        thumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!thumbUpCheck) {
                    if(thumbDownCheck) {
                        thumbDown.setSelected(false);
                        thumbDownNumber = Integer.parseInt(thumbDownNum.getText().toString());
                        thumbDownNumber -= 1;
                        thumbDownNum.setText(String.valueOf(thumbDownNumber));
                        thumbDownCheck = false;
                    }
                    thumbUpCheck = true;
                    thumbUp.setSelected(true);
                    thumbUpNumber = Integer.parseInt(thumbUpNum.getText().toString());
                    thumbUpNumber += 1;
                    thumbUpNum.setText(String.valueOf(thumbUpNumber));
                } else {
                    thumbUpCheck = false;
                    thumbUp.setSelected(false);
                    thumbUpNumber = Integer.parseInt(thumbUpNum.getText().toString());
                    thumbUpNumber -= 1;
                    thumbUpNum.setText(String.valueOf(thumbUpNumber));
                }
            }
        });

        thumbDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!thumbDownCheck) {
                    if(thumbUpCheck) {
                        thumbUp.setSelected(false);
                        thumbUpNumber = Integer.parseInt(thumbUpNum.getText().toString());
                        thumbUpNumber -= 1;
                        thumbUpNum.setText(String.valueOf(thumbUpNumber));
                        thumbUpCheck = false;
                    }
                    thumbDownCheck = true;
                    thumbDown.setSelected(true);
                    thumbDownNumber = Integer.parseInt(thumbDownNum.getText().toString());
                    thumbDownNumber += 1;
                    thumbDownNum.setText(String.valueOf(thumbDownNumber));
                } else {
                    thumbDownCheck = false;
                    thumbDown.setSelected(false);
                    thumbDownNumber = Integer.parseInt(thumbDownNum.getText().toString());
                    thumbDownNumber -= 1;
                    thumbDownNum.setText(String.valueOf(thumbDownNumber));
                }
            }
        });

        writeReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewIntent = new Intent(getApplicationContext(), ReviewActivity.class);
                reviewIntent.putExtra("id", id);
                reviewIntent.putExtra("title", title);
                reviewIntent.putExtra("grade", grade);
                startActivity(reviewIntent);
            }
        });

        viewAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewAllIntent = new Intent(getApplicationContext(), ViewAllActivity.class);
                viewAllIntent.putExtra("id", id);
                viewAllIntent.putExtra("title", title);
                viewAllIntent.putExtra("grade", grade);
                viewAllIntent.putExtra("user_rating", user_rating);
                startActivity(viewAllIntent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        movieGalleryAdapter = new MovieGalleryAdapter(this);

    }

    public void sendRequest() {
        String movieUrl = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=" + id;
        String commentUrl = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=" + id + "&&limit=3";

        Log.d("result2: ", "" + movieUrl + ", " + commentUrl);

        StringRequest movieRequest = new StringRequest(
                Request.Method.GET,
                movieUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processMovieResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("movie error: ", "" + error.getMessage());
                    }
                }
        );

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
                        Log.d("comment error", "" + error.getMessage());
                    }
                }
        );

        movieRequest.setShouldCache(false);
        commentRequest.setShouldCache(false);
        requestQueue.add(movieRequest);
        requestQueue.add(commentRequest);

    }

    public void processMovieResponse(String response) {
        Gson gson = new Gson();
        ReadMovie readMovie = gson.fromJson(response, ReadMovie.class);

        if(readMovie != null) {
            title = readMovie.result.get(0).title;
            date = readMovie.result.get(0).date;
            user_rating = readMovie.result.get(0).user_rating;
            audience_rating = readMovie.result.get(0).audience_rating;
            reservation_rate = readMovie.result.get(0).reservation_rate;
            reservation_grade = readMovie.result.get(0).reservation_grade;
            grade = readMovie.result.get(0).grade;
            thumb = readMovie.result.get(0).thumb;
            photos = readMovie.result.get(0).photos;
            videos = readMovie.result.get(0).videos;
            genre = readMovie.result.get(0).genre;
            duration = readMovie.result.get(0).duration;
            audience = readMovie.result.get(0).audience;
            synopsis = readMovie.result.get(0).synopsis;
            director = readMovie.result.get(0).director;
            actor = readMovie.result.get(0).actor;
            like = readMovie.result.get(0).like;
            dislike = readMovie.result.get(0).dislike;
        }

        Log.d("photos: ", "" + photos);
        Log.d("videos: ", "" + videos);

        if(photos != null) array_photos = photos.split(",");
        if(videos!= null) array_videos = videos.split(",");

        setMovieInfo();

        if(photos != null && videos != null) addItemsToMovieGalleryAdapter();
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

        addItemsToCommentsAdapter();
    }

    public void setMovieInfo() {
        ImageLoadTask imageLoadTask = new ImageLoadTask();
        imageLoadTask.execute(thumb);

        if(grade.equals("12")) movieAgeRating.setImageResource(R.drawable.ic_12);
        else if(grade.equals("15")) movieAgeRating.setImageResource(R.drawable.ic_15);
        else if(grade.equals("19")) movieAgeRating.setImageResource(R.drawable.ic_19);

        String[] splitedDate = date.split("-");
        String year = splitedDate[0];
        String month = splitedDate[1];
        String day = splitedDate[2];

        date = year + "." + month + "." + day;

        movieTitle.setText(title);
        movieReleaseDate.setText(date);
        movieGenre.setText(genre);
        movieDuration.setText(duration);
        thumbUpNum.setText(like);
        thumbDownNum.setText(dislike);
        movieReservationGrade.setText(reservation_grade);
        movieReservationRate.setText(reservation_rate);
        movieAudienceRating.setText(audience_rating);
        movieAudience.setText(audience);
        movieSynopsis.setText(synopsis);
        movieDirector.setText(director);
        movieActor.setText(actor);
    }

    public void addItemsToMovieGalleryAdapter() {
        for(int i = 0; i < array_photos.length; i++) movieGalleryAdapter.addItem(new MovieGalleryItem(array_photos[i], 0));
        for(int i = 0; i < array_videos.length; i++) movieGalleryAdapter.addItem(new MovieGalleryItem(array_videos[i], 1));

        recyclerView.setAdapter(movieGalleryAdapter);

        Log.d("getItemCount: ", "" + movieGalleryAdapter.getItemCount());

        movieGalleryAdapter.setOnItemClickListener(new MovieGalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieGalleryAdapter.ViewHolder holder, View view, int position) {
                MovieGalleryItem item = movieGalleryAdapter.getItem(position);

                if(item.getPhoto() != null) {
                    Toast.makeText(getApplicationContext(), "아이템 선택됨: " + position + ", " + item.getPhoto(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ViewPictureActivity.class);
                    intent.putExtra("pictureUrl", item.getPhoto());
                    startActivity(intent);
                }

                if(item.getVideo() != null) {
                    Toast.makeText(getApplicationContext(), "아이템 선택됨: " + position + ", " + item.getVideo(), Toast.LENGTH_LONG).show();
                    String videoId = item.getVideo().replace("https://youtu.be/", "");
                    watchYoutubeVideo(videoId);
                }
            }
        });
    }

    public void watchYoutubeVideo(String id) {
        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            startActivity(intent2);
        }
    }

    public void addItemsToCommentsAdapter() {

        for(int i = 0; i < count; i++) {
            String time = calculateTime(commentsTime.get(i));
            commentsAdapter.addItem(new CommentsItem(commentsId.get(i), commentsWriter.get(i), time, commentsRating.get(i), commentsContent.get(i), commentsRecommend.get(i)));
        }

        Log.d("TAG", "" + commentsAdapter.getCount());

        listView.setAdapter(commentsAdapter);

        setListViewHeightBasedOnChildren(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CommentsItem commentsItem = (CommentsItem) commentsAdapter.getItem(i);
                Toast.makeText(getApplicationContext(), "선택:" + commentsItem.getId(), Toast.LENGTH_LONG).show();
            }
        });
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null) return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);

        int totalHeight = 0;
        View view = null;

        for(int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);

            if(i == 0) view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_detail);
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            Toast.makeText(this, "첫번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_review) {
            Toast.makeText(this, "두번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_book) {
            Toast.makeText(this, "세번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_detail);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                if(bitmapHashMap.containsKey(url)) {
                    Bitmap oldBitmap = bitmapHashMap.remove(url);
                    if(oldBitmap != null) {
                        oldBitmap.recycle();
                        oldBitmap = null;
                    }
                }

                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmapHashMap.put(strings[0], bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            movieMainImg.setImageBitmap(bitmap);
            movieMainImg.invalidate();
        }
    }
}

