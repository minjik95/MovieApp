package com.example.movieapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListPopupAdapter extends BaseAdapter {
    private ArrayList<ListPopupItem> items;
    private ListPopupViewHolder viewHolder;

    public ListPopupAdapter(ArrayList<ListPopupItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ListPopupItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_popup_item, null);
            viewHolder = new ListPopupViewHolder(view);
        }

        viewHolder.imageView.setImageResource(getItem(i).getImage());
        viewHolder.textView.setText(getItem(i).getOrder());

        return view;
    }

    public class ListPopupViewHolder {
        ImageView imageView;
        TextView textView;

        ListPopupViewHolder(View view) {
            imageView = view.findViewById(R.id.listPopupImage);
            textView = view.findViewById(R.id.listPopupTextView);
        }
    }
}
