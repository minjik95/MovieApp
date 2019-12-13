package com.example.movieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.net.URL;
import java.util.HashMap;

public class ViewPictureActivity extends AppCompatActivity {

    private Toolbar toolbar;
    // private ImageView back;
    private ImageView picture;

    private String pictureUrl;
    private static HashMap<String, Bitmap> bitmapHashMap = new HashMap<>();

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpicture);

        toolbar = (Toolbar) findViewById(R.id.toolbar_picture);
        picture = (ImageView) findViewById(R.id.picture);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
        getSupportActionBar().setTitle("사진 보기");

        Intent intent = getIntent();
        pictureUrl = intent.getExtras().getString("pictureUrl");

        ImageLoadTask imageLoadTask = new ImageLoadTask();
        imageLoadTask.execute(pictureUrl);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) { // toolbar의 back키
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

            picture.setImageBitmap(bitmap);
            picture.invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= scaleGestureDetector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));
            picture.setScaleX(scaleFactor);
            picture.setScaleY(scaleFactor);
            return true;
        }
    }

}
