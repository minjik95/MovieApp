package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ListView listView;
    private CommentsAdapter adapter;
    private Button thumbUp;
    private Button thumbDown;
    private TextView thumbUpNum;
    private TextView thumbDownNum;
    private int thumbUpNumber = 0;
    private int thumbDownNumber = 0;
    private boolean thumbUpCheck = false;
    private boolean thumbDownCheck = false;
    private TextView writeReviews;
    private Button viewAllComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_detail);
        navigationView = (NavigationView) findViewById(R.id.nav_view_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        listView = (ListView) findViewById(R.id.listView);
        thumbUp = (Button) findViewById(R.id.thumb_up);
        thumbDown = (Button) findViewById(R.id.thumb_down);
        thumbUpNum = (TextView) findViewById(R.id.thumb_up_num);
        thumbDownNum = (TextView) findViewById(R.id.thumb_down_num);
        writeReviews = (TextView) findViewById(R.id.writeReviews);
        viewAllComments = (Button) findViewById(R.id.viewAllComments);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("영화 상세");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);

        navigationView.setNavigationItemSelectedListener(this);

        adapter = new CommentsAdapter();
        adapter.addItem(new CommentsItem("minjik95", 10));
        adapter.addItem(new CommentsItem("junkuk98", 5));
        adapter.addItem(new CommentsItem("minjik1225", 15));
        adapter.addItem(new CommentsItem("solongmypartner", 20));
        adapter.addItem(new CommentsItem("heyyy", 25));

        Log.d("TAG", "" + adapter.getCount());

        listView.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CommentsItem commentsItem = (CommentsItem) adapter.getItem(i);
                Toast.makeText(getApplicationContext(), "선택:" + commentsItem.getId(), Toast.LENGTH_LONG).show();
            }
        });

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
                startActivity(reviewIntent);
            }
        });

        viewAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewAllIntent = new Intent(getApplicationContext(), ViewAllActivity.class);
                startActivity(viewAllIntent);
            }
        });

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_detail);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

