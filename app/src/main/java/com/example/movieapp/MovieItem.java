package com.example.movieapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;

public class MovieItem extends Fragment {
    private TextView tv_reservation_grade;
    private TextView tv_title;
    private TextView tv_reservation_rate;
    private TextView tv_grade;
    private ImageView iv_image;

    private Context context;
    private String id;
    private String reservation_grade;
    private String title;
    private String reservation_rate;
    private String grade;
    private String image;
    private String imagePath;

    private static HashMap<String, Bitmap> bitmapHashMap = new HashMap<>();

    MovieItem() {}

    MovieItem(Context context, String id, String reservation_grade, String title, String reservation_rate, String grade, String image) {
        this.context = context;
        this.id = id;
        this.reservation_grade = reservation_grade;
        this.title = title;
        this.reservation_rate = reservation_rate;
        this.grade = grade;
        this.image = image;
    }

    MovieItem(Context context, String id, String reservation_grade, String title, String reservation_rate, String grade, String image, String imagePath) {
        this.context = context;
        this.id = id;
        this.reservation_grade = reservation_grade;
        this.title = title;
        this.reservation_rate = reservation_rate;
        this.grade = grade;
        this.image = image;
        this.imagePath = imagePath;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_movieitem, container, false);
        ViewGroup viewGroup = (ViewGroup) v;

        tv_reservation_grade = (TextView) v.findViewById(R.id.reservation_grade);
        tv_title = (TextView) v.findViewById(R.id.title);
        tv_reservation_rate = (TextView) v.findViewById(R.id.reservation_rate);
        tv_grade = (TextView) v.findViewById(R.id.grade);
        iv_image = (ImageView) v.findViewById(R.id.image);

        tv_reservation_grade.setText(reservation_grade);
        tv_title.setText(title);
        tv_reservation_rate.setText(reservation_rate);

        if(grade.equals("19")) tv_grade.setText("청소년 관람불가");
        else tv_grade.setText(grade + "세 관람가");


        if(image != null) {
            Log.d("image1: ", "" + image);
            ImageLoadTask imageLoadTask = new ImageLoadTask();
            imageLoadTask.execute(image);
        } else {
            Log.d("image2: ", "" + image);
            loadImageFromStorage(imagePath, id);
        }

        Button btn_detail = (Button) v.findViewById(R.id.detail);
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        return viewGroup;
    }

    private void loadImageFromStorage(String path, String imageName) {
        try {
            File file = new File(path, imageName + ".png");
            Log.d("result6: ", "" + file);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            iv_image.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

            iv_image.setImageBitmap(bitmap);
            iv_image.invalidate();
        }
    }

}

