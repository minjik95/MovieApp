package com.example.movieapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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

    private TextView movieOrder;
    private ListPopupWindow listPopupWindow;
    // private boolean isOpen = false;
    private String order = "1";

    public SQLiteDatabase database;
    private String databaseName = "Movie";
    private int databaseVersion = 1;
    private String tableMovieList = "MovieList";
    private String tableMovieDetail = "MovieDetail";
    private String tableComment = "MovieComment";

    private BroadcastReceiver networkChangeReceiver;

    private int status;

    private String imagePath = " /data/user/0/com.example.movieapp/app_imageDirectory";

    /*
    *
    *
    *
    *연결이 될 때 data를 로드시키고 연결이 안되면 데이터베이스에서 data를 로드...
    *
    *app_imageDirectory가 안생김...
    *
    *
    *
    *
    *
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);

        DatabaseHelper helper = new DatabaseHelper(this, databaseName, null, databaseVersion);
        database = helper.getWritableDatabase();

        if(requestQueue == null) requestQueue = Volley.newRequestQueue(getApplicationContext());

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

        movieOrder = (TextView) findViewById(R.id.movieOrder);

        final ArrayList<ListPopupItem> listPopupItems = new ArrayList<>();
        listPopupItems.add(new ListPopupItem(R.drawable.order_small, "예매율순"));
        listPopupItems.add(new ListPopupItem(R.drawable.order_small, "큐레이션"));
        listPopupItems.add(new ListPopupItem(R.drawable.order_small, "상영예정"));

        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ListPopupAdapter(listPopupItems));
        listPopupWindow.setAnchorView(movieOrder);
        listPopupWindow.setWidth(500);
        listPopupWindow.setHeight(400);
        listPopupWindow.setAnimationStyle(R.style.AnimationTheme);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                order = String.valueOf(i + 1);
                sendRequest(order);
                movieOrder.setText(listPopupItems.get(i).getOrder());
                listPopupWindow.dismiss();
            }
        });
        movieOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               /* if(isOpen) {
                    listPopupWindow.setAnimationStyle(R.style.UpAnimationTheme);
                    Log.d("isOpen1: ", "" + isOpen);
                    isOpen = false;
                } else {
                    listPopupWindow.setAnimationStyle(R.style.DownAnimationTheme);
                    Log.d("isOpen2: ", "" + isOpen);
                    isOpen = true;
                }
               */
                listPopupWindow.show();
            }
        });


        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            Log.d("status", "" + "lte or wifi");
            sendRequest(order);
        }
        if(status == NetworkStatus.TYPE_NOT_CONNECTED) {
            Log.d("status", "" + "not connected");
            loadDataFromDatabase();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(networkChangeReceiver != null)
            unregisterReceiver(networkChangeReceiver);
    }

    public void sendRequest(String order) {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovieList?type=" + order;
        Log.d("url: ", "" + url);
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
        String sql;

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

                /*sql = "SELECT * FROM " + tableMovieList;*/
                sql = "SELECT * FROM " + tableMovieList + " WHERE _id = " + readMovieList.result.get(i).id;
                Cursor cursor = database.rawQuery(sql, null);
                Log.d("result1: ", "" + cursor.getCount() + ", " + sql);

                if (cursor.getCount() <= 0) {
                    ImageLoadTask imageLoadTask = new ImageLoadTask();
                    imageLoadTask.execute(readMovieList.result.get(i).image, readMovieList.result.get(i).id);


                    sql = "INSERT INTO " + tableMovieList + "(_id, reservation_grade, title, reservation_rate, grade, image, imagePath)" + " VALUES(?, ?, ?, ?, ?, ?, ?)";
                    Object[] params = {readMovieList.result.get(i).id, readMovieList.result.get(i).reservation_grade, readMovieList.result.get(i).title, readMovieList.result.get(i).reservation_rate, readMovieList.result.get(i).grade, readMovieList.result.get(i).image, imagePath};
                    database.execSQL(sql, params);
                } else {

                }
            }

            addToViewPagerAdapter();
        }
    }

    public void addToViewPagerAdapter() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        for(int i = 0; i < count; i++) {
            movieItems.add(new MovieItem(getApplicationContext(), ids.get(i), reservation_grades.get(i), titles.get(i), reservation_rates.get(i), grades.get(i), images.get(i)));
            viewPagerAdapter.addItem(movieItems.get(i));
        }

        ids = new ArrayList<>();
        reservation_grades = new ArrayList<>();
        titles = new ArrayList<>();
        reservation_rates = new ArrayList<>();
        grades = new ArrayList<>();
        images = new ArrayList<>();
        movieItems = new ArrayList<>();

        viewPager.setAdapter(viewPagerAdapter);
    }

    public void loadDataFromDatabase() {
        String sql = "SELECT _id, reservation_grade, title, reservation_rate, grade, image, imagePath FROM " + tableMovieList;
        Cursor cursor = database.rawQuery(sql, null);

        Log.d("result2: ", "" + cursor.getCount());

        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            String id = cursor.getString(0);
            String reservation_grade = cursor.getString(1);
            String title = cursor.getString(2);
            String reservation_rate = cursor.getString(3);
            String grade = cursor.getString(4);
            String image = cursor.getString(5);
            String imagePath = cursor.getString(6);

            Log.d("result3: ", "" + id + ", " + reservation_grade + ", " + title + ", " + reservation_rate + ", " + grade + ", " + "null" + ", " + imagePath);

            movieItems.add(new MovieItem(getApplicationContext(), id, reservation_grade, title, reservation_rate, grade, null, imagePath));
            viewPagerAdapter.addItem(movieItems.get(i));
        }

        viewPager.setAdapter(viewPagerAdapter);
    }

    private void saveToInternalStorage(Bitmap bitmap, String imageName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDirectory", Context.MODE_PRIVATE);
        Log.d("result4: ", "" + directory);
        File myPath = new File(directory, imageName + ".png");
        Log.d("result5: ", "" + myPath);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        } else if (id == R.id.nav_review) {
            Toast.makeText(this, "두번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_book) {
            Toast.makeText(this, "세번째 메뉴 선택됨", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_list);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;
        String imageName;

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                imageName = strings[1];

                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            saveToInternalStorage(bitmap, imageName);
        }
    }
}
