package com.example.photos;

import android.os.Parcel;
import android.os.Parcelable;

public class Connection implements Parcelable {
    //fields
    private String photoPath;
    protected String aName;

    //constructor
    public Connection(String album, String path) {
        this.photoPath = path;
        this.aName = album;
    }

    protected Connection(Parcel in) {
        photoPath = in.readString();
        aName = in.readString();
    }

    public static final Creator<Connection> CREATOR = new Creator<Connection>() {
        @Override
        public Connection createFromParcel(Parcel in) {
            return new Connection(in);
        }

        @Override
        public Connection[] newArray(int size) {
            return new Connection[size];
        }
    };

    //gets photo path
    public String getPath() {
        return photoPath;
    }

    //gets album name
    public String getAlbum() {
        return aName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoPath);
        dest.writeString(aName);
    }
}
