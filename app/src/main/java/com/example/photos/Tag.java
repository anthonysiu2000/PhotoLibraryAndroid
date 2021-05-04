package com.example.photos;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {

    //private fields
    private String tagName;
    private String tagValue;

    //constructor
    public Tag(String name, String value) {
        this.tagName = name;
        this.tagValue = value;
    }

    protected Tag(Parcel in) {
        tagName = in.readString();
        tagValue = in.readString();
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    //get name
    public String getName() {
        return tagName;
    }

    //get tag value
    public String getValue() {
        return tagValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tagName);
        dest.writeString(tagValue);
    }
}
