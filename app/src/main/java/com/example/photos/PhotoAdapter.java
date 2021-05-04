package com.example.photos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class PhotoAdapter extends ArrayAdapter {
    private Integer[] imageid;
    private Activity context;

    public PhotoAdapter(Activity context, Integer[] imageid) {
        super(context, R.layout.thumbnail, imageid);
        this.context = context;
        this.imageid = imageid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.thumbnail, null, true);
        ImageView imageView = (ImageView) row.findViewById(R.id.imageView);

        imageView.setImageResource(imageid[position]);
        return  row;
    }
}