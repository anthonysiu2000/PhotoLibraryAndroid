package com.example.photos;

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

    //constructor
    public Photo(File imageFile) {
        this.filePath = imageFile.getPath();
        Date date = new Date(imageFile.lastModified());
        this.tags = new ArrayList<Tag>();

    }

    protected Photo(Parcel in) {
        filePath = in.readString();
        tags = in.createTypedArrayList(Tag.CREATOR);
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
        dest.writeList(tags);
    }
}
