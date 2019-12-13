package com.example.movieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieGalleryAdapter extends RecyclerView.Adapter<MovieGalleryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MovieGalleryItem> items = new ArrayList<>();

    private ImageView movieGallery;
    private ImageView moviePlay;

    private OnItemClickListener listener;

    private static HashMap<String, Bitmap> bitmapHashMap = new HashMap<>();

    public interface OnItemClickListener {
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public MovieGalleryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.movie_gallery, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieGalleryItem item = items.get(position);

        holder.setItem(item);
        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MovieGalleryItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<MovieGalleryItem> items) {
        this.items = items;
    }

    public MovieGalleryItem getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        OnItemClickListener listener;

        public ViewHolder(final View itemView) {
            super(itemView);
            movieGallery = itemView.findViewById(R.id.movieGallery);
            moviePlay = itemView.findViewById(R.id.moviePlay);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(MovieGalleryItem item) {
            ImageLoadTask imageLoadTask = new ImageLoadTask();

            if(item.getPhoto() != null) {
                Log.d("result: ",  "" + item.getPhoto());
                imageLoadTask.execute(item.getPhoto());
            }

            if (item.getVideo() != null) {
                String videoId = item.getVideo().replace("https://youtu.be/", "");
                String url = "https://img.youtube.com/vi/"+ videoId +"/0.jpg";
                Log.d("url: ", "" + url);
                imageLoadTask.execute(url);
                moviePlay.setVisibility(View.VISIBLE);
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
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

            movieGallery.setImageBitmap(bitmap);
            movieGallery.invalidate();
        }
    }

}
