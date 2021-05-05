package com.example.photos;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Photo implements Parcelable {
    //fields
    private String filePath;
    protected ArrayList<Tag> tags;
    public Bitmap bitmap;

    //constructor
    public Photo(String filePath) {
        this.filePath = filePath;
        this.tags = new ArrayList<Tag>();
        this.bitmap = null;

    }


    protected Photo(Parcel in) {
        filePath = in.readString();
        tags = in.createTypedArrayList(Tag.CREATOR);
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    //gets path
    public String getPath() {
        return filePath;
    }

    //gets Tags
    public ArrayList<Tag> getTags() {
        return tags;
    }

    //adds a tag
    public void addTag(Tag inputTag) {
        tags.add(inputTag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeTypedList(tags);
        dest.writeParcelable(bitmap, flags);
    }
}
