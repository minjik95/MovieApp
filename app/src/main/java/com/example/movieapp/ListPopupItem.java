package com.example.movieapp;

public class ListPopupItem {
    private int image;
    private String order;

    public ListPopupItem(int image, String order) {
        this.image = image;
        this.order = order;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
