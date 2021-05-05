package com.example.photos;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class User implements Parcelable {
    //fields
    private ArrayList<Album> albumList;
    private ArrayList<Photo> photoList;
    private ArrayList<Connection> conList;
    private int albumNumCreate;

    //constructor
    public User(){
        this.albumList = new ArrayList<>();
        this.photoList = new ArrayList<>();
        this.conList = new ArrayList<>();
        this.albumNumCreate = 0;
    }

    protected User(Parcel in) {
        albumList = in.createTypedArrayList(Album.CREATOR);
        albumNumCreate = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //gets list of albums for this user
    public ArrayList<Album> getAlbums() {
        return albumList;
    }

    //gets list of photos for this user
    public ArrayList<Photo> getPhotos() {
        return photoList;
    }

    //gets list of connections of this user
    public ArrayList<Connection> getConnections() {
        return conList;
    }


    //creates a new album; returns true if successful
    public boolean createAlbum(String aName) {
        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getName().equals(aName)) {
                return false;
            }
        }
        albumList.add(new Album(aName));
        return true;
    }

    //deletes an album; returns true if successful
    public boolean deleteAlbum(String aName) {
        boolean foundAlbum = false;
        for (int i = albumList.size()-1; i >= 0; i--) {
            if (albumList.get(i).getName().equals(aName)) {
                albumList.remove(i);
                foundAlbum = true;
            }
        }
        if (!foundAlbum) {
            return false;
        }
        //removes connections linked to albums
        for (int j = conList.size()-1; j >= 0; j--) {
            if (conList.get(j).getAlbum().equals(aName)) {
                conList.remove(j);
            }
        }
        //removes photos with connection to this album only
        for (int k = photoList.size()-1; k >= 0; k--) {
            boolean photoFound = false;
            String photoPath = photoList.get(k).getPath();
            for (int l = 0; l < conList.size(); l++) {
                if (photoPath.equals(conList.get(l).getPath())) {
                    photoFound = true;
                }
            }
            if (!photoFound) {
                photoList.remove(k);
            }
        }
        return true;
    }

    //renames an album; returns true if successful
    public boolean renameAlbum(String oName, String nName) {
        boolean foundAlbum = false;
        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getName().equals(oName)) {
                albumList.get(i).aName = nName;
                foundAlbum = true;
            }
        }
        if (!foundAlbum) {
            return false;
        }
        for (int j = 0; j < conList.size(); j++) {
            if (conList.get(j).getAlbum().equals(oName)) {
                conList.get(j).aName = nName;
            }
        }
        return true;
    }

    //adds a new photo into an album: returns true if successful
    public boolean addPhoto(String aName, String photoPath, Bitmap bitmap) {
        Photo photo = new Photo(photoPath);
        photo.bitmap = bitmap;
        boolean albumFound = false;
        boolean photoFound = false;
        //finds the album that we want to insert photo
        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getName().equals(aName)) {
                albumFound = true;
                break;
            }
        }
        if (!albumFound) {
            return false;
        }
        //checks if a connection is already established between photo and album
        for (int j = 0; j < conList.size(); j++) {
            if (conList.get(j).getAlbum().equals(aName)) {
                if (conList.get(j).getPath().equals(photo.getPath())) {
                    return false;
                }
            }
        }
        //if photo does not exist in database, adds it
        for (int k = 0; k < photoList.size(); k++) {
            if (photoList.get(k).getPath().equals(photo.getPath())) {
                photoFound = true;
                break;
            }
        }
        if (!photoFound) {
            photoList.add(photo);
        }
        //inserts a new connection
        Connection con = new Connection(aName, photo.getPath());
        conList.add(con);
        return true;
    }

    //removes a photo from an album; returns true if successful
    public boolean removePhoto(String aName, String photoPath) {
        boolean albumFound = false;
        boolean conFound = false;
        int numCon = 0;
        //finds the album that we want to remove photo
        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getName().equals(aName)) {
                albumFound = true;
                break;
            }
        }
        if (!albumFound) {
            return false;
        }
        //finds photo in connection list and removes it
        for (int j = conList.size()-1; j >= 0; j--) {
            if (conList.get(j).getPath().equals(photoPath)) {
                if (conList.get(j).getAlbum().equals(aName)) {
                    conList.remove(j);
                    conFound = true;
                }
                numCon++;
            }
        }
        if (!conFound) {
            return false;
        }
        //finds photo in the album and removes it if no other album references it
        if (numCon == 1) {
            for (int k = photoList.size()-1; k >= 0; k--) {
                if (photoList.get(k).getPath().equals(photoPath)) {
                    photoList.remove(k);
                }
            }
        }
        return true;
    }

    //adds a tag to a specified photo; returns true if successful
    public boolean addTag(String photoPath, String tagName, String tagValue) {
        //checks if the photo exists
        Tag tag = new Tag(tagName, tagValue);
        for (int k = 0; k < photoList.size(); k++) {
            if (photoList.get(k).getPath().equals(photoPath)) {
                for (int i = 0; i < photoList.get(k).getTags().size(); i++) {
                    if (photoList.get(k).getTags().get(i).getName().equals(tagName)) {
                        if (photoList.get(k).getTags().get(i).getValue().equals(tagValue)) {
                            return false;
                        }
                    }
                }
                photoList.get(k).addTag(tag);
                return true;
            }
        }
        return false;
    }

    //removes a tag of a specified photo; returns true if successful
    public boolean removeTag(String photoPath, String tagName, String tagValue) {
        //checks if the photo exists
        for (int k = 0; k < photoList.size(); k++) {
            if (photoList.get(k).getPath().equals(photoPath)) {
                //checks if the tag exists
                for (int i = photoList.get(k).tags.size()-1; i >= 0; i--) {
                    if (photoList.get(k).tags.get(i).getName().equals(tagName) &&
                            photoList.get(k).tags.get(i).getValue().equals(tagValue)) {
                        photoList.get(k).tags.remove(i);
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    //copies a photo from an album to another; returns true if successful
    public boolean copyPhoto(String oAlbName, String nAlbName, String photoPath) {
        if (oAlbName.equals(nAlbName)) {
            return false;
        }
        //checks if the photo exists
        for (int k = 0; k < photoList.size(); k++) {
            if (photoList.get(k).getPath().equals(photoPath)) {
                //checks that the album exists
                boolean albumExists = false;
                for (int j = 0; j < albumList.size(); j++) {
                    if (albumList.get(j).getName().equals(nAlbName)) {
                        albumExists = true;
                    }
                }
                if (!albumExists) {
                    return false;
                }
                //checks if there is already an instance of the photo in the target album
                for (int i = 0; i < conList.size(); i++) {
                    if (conList.get(i).getAlbum().equals(nAlbName) && conList.get(i).getPath().equals(photoPath)) {
                        return false;
                    }
                }
                conList.add(new Connection(nAlbName, photoPath));
                return true;
            }
        }
        return false;
    }

    //moves a photo from an album to another; returns true if successful
    public boolean movePhoto(String oAlbName, String nAlbName, String photoPath) {
        if (oAlbName.equals(nAlbName)) {
            return false;
        }
        //copies photo to new album
        if (copyPhoto(oAlbName, nAlbName, photoPath)) {
            //removes old photo to album connection
            for (int i = conList.size()-1; i >= 0; i--) {
                if (conList.get(i).getAlbum().equals(oAlbName) && conList.get(i).getPath().equals(photoPath)) {
                    conList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    //returns an array of photos that have the tags specified; if 2, uses andOp boolean to specify "and" or "or"
    public ArrayList<Photo> searchPhotoByTag(Tag tag1, Tag tag2, boolean andOp) {
        //creates new array list
        ArrayList<Photo> sortedPhotos = new ArrayList<>(photoList);
        //removes photos that do not have the specified tags
        for (int i = sortedPhotos.size() - 1; i >= 0; i--) {
            ArrayList<Tag> temp = sortedPhotos.get(i).getTags();
            if (temp.size() == 0) {
                sortedPhotos.remove(i);
            }
            //if there is only one tag
            if (tag2 == null) {
                for (int j = 0; j< temp.size(); j++) {
                    boolean remove = true;
                    if (temp.get(j).getName().equals(tag1.getName()) && temp.get(j).getValue().equals(tag1.getValue())) {
                        remove = false;
                    }
                    if (remove) {
                        sortedPhotos.remove(i);
                    }
                }
            }
            //two tags
            else {
                for (int j = 0; j< temp.size(); j++) {
                    boolean tag1Found = false;
                    boolean tag2Found = false;
                    //sets fields to true if tags are found
                    if (temp.get(j).getName().equals(tag1.getName()) && temp.get(j).getValue().equals(tag1.getValue())) {
                        tag1Found = true;
                    }
                    if (temp.get(j).getName().equals(tag2.getName()) && temp.get(j).getValue().equals(tag2.getValue())) {
                        tag2Found = true;
                    }
                    //remove those with no tags
                    if (!tag1Found && !tag2Found) {
                        sortedPhotos.remove(i);
                    }
                    //In AND case, remove those with only one tag
                    else if (andOp && (tag1Found ^ tag2Found)) {
                        sortedPhotos.remove(i);
                    }
                }
            }
        }
        return sortedPhotos;
    }

    //creates a new album and adds connections between the album and the copied photos
    public void createAlbumFromSearch(ArrayList<Photo> list) {
        albumNumCreate += 1;
        String newAlbumName = "CreatedAlbum" + String.valueOf(albumNumCreate);
        createAlbum(newAlbumName);
        for (int i = 0; i < list.size(); i++) {
            //inserts a new connection
            Connection con = new Connection(newAlbumName, list.get(i).getPath());
            conList.add(con);
        }
    }

    //returns number of photos in an album
    public int getAlbumNum(String aName) {
        int numPhotos = 0;
        for (int i = 0; i < conList.size(); i++) {
            if (conList.get(i).getAlbum().equals(aName)) {
                numPhotos++;
            }
        }
        return numPhotos;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(albumList);
        dest.writeTypedList(photoList);
        dest.writeTypedList(conList);
        dest.writeInt(albumNumCreate);
    }
}
