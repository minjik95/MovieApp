package com.example.movieapp;

public class CommentsItem {
    String id;
    String writer;
    String time;
    float rating;
    String content;
    String recommend;

    public CommentsItem(String id, String writer, String time, float rating, String content, String recommend) {
        this.id = id;
        this.writer = writer;
        this.time = time;
        this.rating = rating;
        this.content = content;
        this.recommend = recommend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}
