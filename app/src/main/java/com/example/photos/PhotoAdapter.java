package com.example.photos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class PhotoAdapter extends ArrayAdapter {
    private ArrayList<Bitmap> bitmapList;
    private Activity context;

    public PhotoAdapter(Activity context, ArrayList<Bitmap> bitmapList) {
        super(context, R.layout.thumbnail, bitmapList);
        this.context = context;
        this.bitmapList = bitmapList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.thumbnail, null, true);
        ImageView imageView = (ImageView) row.findViewById(R.id.imageView);

        imageView.setImageBitmap(bitmapList.get(position));
        return  row;
    }
}