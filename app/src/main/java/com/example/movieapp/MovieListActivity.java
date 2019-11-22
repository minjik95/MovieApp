package com.example.movieapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RequestQueue requestQueue;

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private int count;

    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<String> reservation_grades = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> reservation_rates = new ArrayList<>();
    private ArrayList<String> grades = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();

    private ArrayList<MovieItem> movieItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);

        if(requestQueue == null) requestQueue = Volley.newRequestQueue(getApplicationContext());
        sendRequest();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("영화 목록");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_list);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_list);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);

        /*viewPager.setClipToPadding(false);
        viewPager.setPadding(20,0,20,0);
        viewPager.setPageMargin(10);*/

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    }

    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovieList?type=1";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("responseError", "" + error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        requestQueue.add(request);
    }
    public void processResponse(String response) {
        Gson gson = new Gson();
        ReadMovieList readMovieList = gson.fromJson(response, ReadMovieList.class);

        if(readMovieList != null) {
            count = readMovieList.result.size();

            for(int i = 0; i < count; i++) {
                ids.add(readMovieList.result.get(i).id);
                reservation_grades.add(readMovieList.result.get(i).reservation_grade);
                titles.add(readMovieList.result.get(i).title);
                reservation_rates.add(readMovieList.result.get(i).reservation_rate);
                grades.add(readMovieList.result.get(i).grade);
                images.add(readMovieList.result.get(i).image);
            }

            addToViewPagerAdapter();
        }
    }

    public void addToViewPagerAdapter() {
        for(int i = 0; i < count; i++) {
            movieItems.add(new MovieItem(ids.get(i), reservation_grades.get(i), titles.get(i), reservation_rates.get(i), grades.get(i), images.get(i)));
            viewPagerAdapter.addItem(movieItems.get(i));
        }

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_list);
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            Toast.makeText(this, "첫번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
            /*Intent listIntent = new Intent(getApplicationContext(), MovieListActivity.class);
            startActivity(listIntent);*/
        } else if (id == R.id.nav_review) {
            Toast.makeText(this, "두번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
            /*Intent reviewIntent = new Intent(getApplicationContext(), ReviewActivity.class);
            startActivity(reviewIntent);*/
        } else if (id == R.id.nav_book) {
            Toast.makeText(this, "세번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
            /*Intent bookIntent = new Intent(getApplicationContext(), MovieListActivity.class);
            startActivity(bookIntent);*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_list);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
