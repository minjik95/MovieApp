package com.example.movieapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {
    private String sql;

    private String tableMovieList = "MovieList";
    private String tableMovieDetail = "MovieDetail";
    private String tableComment = "MovieComment";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sql = "CREATE TABLE if not exists " + tableMovieList + "(_id text PRIMARY KEY, reservation_grade text, title text, reservation_rate text, grade text, image text, imagePath text)";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE if not exists " + tableMovieDetail + "(_id text PRIMARY KEY, title text, date text, user_rating real, audience_rating text, reservation_rate text, reservation_grade text, grade text, thumb text, genre text, duration text, audience text, synopsis text, director text, actor text, _like text, dislike text)";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE if not exists " + tableComment + "(_id text PRIMARY KEY, writer text, time text, rating real, content text, recommend text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // i: oldVersion i1: newVersion
        if(i1 > 1) {
/*            sqLiteDatabase.execSQL("drop table if exists movieList");*/
        }
    }
}
