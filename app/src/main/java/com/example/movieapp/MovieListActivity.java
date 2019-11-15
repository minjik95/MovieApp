package com.example.movieapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;

public class MovieListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private MovieItem1 movieItem1;
    private MovieItem2 movieItem2;
    private MovieItem3 movieItem3;
    private MovieItem4 movieItem4;
    private MovieItem5 movieItem5;
    private MovieItem6 movieItem6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_list);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_list);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);

        /*viewPager.setClipToPadding(false);
        viewPager.setPadding(20,0,20,0);
        viewPager.setPageMargin(10);*/

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        movieItem1 = new MovieItem1();
        viewPagerAdapter.addItem(movieItem1);

        movieItem2 = new MovieItem2();
        viewPagerAdapter.addItem(movieItem2);

        movieItem3 = new MovieItem3();
        viewPagerAdapter.addItem(movieItem3);

        movieItem4 = new MovieItem4();
        viewPagerAdapter.addItem(movieItem4);

        movieItem5 = new MovieItem5();
        viewPagerAdapter.addItem(movieItem5);

        movieItem6 = new MovieItem6();
        viewPagerAdapter.addItem(movieItem6);

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
