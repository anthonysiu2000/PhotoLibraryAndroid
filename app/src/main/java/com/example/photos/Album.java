package com.example.photos;


import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {
    //private fields
    protected String aName;

    //constructor
    public Album(String name) {
        this.aName = name;
    }

    protected Album(Parcel in) {
        aName = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String getName() {
        return aName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(aName);
    }
}
