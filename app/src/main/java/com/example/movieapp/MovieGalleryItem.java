package com.example.movieapp;

public class MovieGalleryItem {
    String photo;
    String video;

    public MovieGalleryItem(String photoOrVideo, int type) {
        if (type == 0) this.photo = photoOrVideo;
        if (type == 1) this.video = photoOrVideo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

}
