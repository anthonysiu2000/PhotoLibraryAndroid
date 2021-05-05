package com.example.photos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.util.ArrayList;

public class AlbumView  extends AppCompatActivity {

    private ListView listView;
    private User user;
    private int albIndex;
    private int currIndex = -1;
    private ArrayList<String> pathList;
    private ArrayList<Bitmap> bitmapList;

    public static final int DISPLAY_PHOTO_CODE = 1;
    public static final int ADD_PHOTO_CODE = 2;
    public static final int MOVE_PHOTO_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // sets back arrow to send info back to userview
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("USER", user);

                // send back to caller
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("USER");
            albIndex = bundle.getInt("INDEX");
        }

        //gets array of paths
        pathList = new ArrayList<>();
        bitmapList = new ArrayList<>();
        if (user.getConnections() != null) {
            for (int i = 0; i < user.getConnections().size(); i++) {
                //finds connections that have the same album as this one
                String conAlbumName = user.getConnections().get(i).getAlbum();
                String thisAlbumName = user.getAlbums().get(albIndex).getName();
                if (conAlbumName.equals(thisAlbumName)) {
                    pathList.add(user.getConnections().get(i).getPath());

                }
            }
        }
        System.out.println("gets to this pointaa");
        if (pathList.size() > 0) {
            System.out.println("gets to this pointaa");
            for (int i = 0; i < pathList.size(); i++) {
                for (int j = 0; j < user.getPhotos().size(); j++) {
                    if (user.getPhotos().get(j).getPath().equals(pathList.get(i))) {
                        bitmapList.add(user.getPhotos().get(j).bitmap);
                    }
                }
            }
        }

        // get the fields and sets the list view to thumbnails
        listView = findViewById(R.id.literal_photo_list);
        //listView.setAdapter(
        //        new PhotoAdapter(this, pathList));
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, bitmapList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                currIndex = position;
            }
        });
    }


    public void displayPhoto(View view) {

        //If no Photos, output error
        if (pathList.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Photos to select from");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //If no Photo selected, output error
        if (currIndex < 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Photo selected");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //goes into display photo instance
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        bundle.putInt("INDEX", albIndex);
        bundle.putString("PATH", pathList.get(currIndex));
        Intent intent = new Intent(this, PhotoEdit.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, DISPLAY_PHOTO_CODE);
    }

    public void addPhoto(View view) {

        //selects image
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtras(bundle);
        intent.setType("image/*");
        System.out.println("gets to this pointzzz");
        startActivityForResult(intent, ADD_PHOTO_CODE);
        System.out.println("gets to this pointzzz");
        currIndex = -1;
    }

    public void deletePhoto(View view) {

        //If no Photos, output error
        if (pathList.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Photos to select from");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            currIndex = -1;
            return;
        }

        //If no Photo selected, output error
        if (currIndex < 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Photo selected");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            currIndex = -1;
            return;
        }

        //Deletes photo from user database
        String photoDeletedPath = pathList.get(currIndex);
        String aName = user.getAlbums().get(albIndex).getName();
        boolean deleted = user.removePhoto(aName, photoDeletedPath);
        if (!deleted) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "Photo Not Deleted Properly");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            currIndex = -1;
            return;
        }

        //gets array of paths
        pathList = new ArrayList<>();
        bitmapList = new ArrayList<>();
        if (user.getConnections() != null) {
            for (int i = 0; i < user.getConnections().size(); i++) {
                //finds connections that have the same album as this one
                String conAlbumName = user.getConnections().get(i).getAlbum();
                String thisAlbumName = user.getAlbums().get(albIndex).getName();
                if (conAlbumName.equals(thisAlbumName)) {
                    pathList.add(user.getConnections().get(i).getPath());

                }
            }
        }
        if (pathList.size() != 0) {
            for (int i = 0; i < pathList.size(); i++) {
                for (int j = 0; j < user.getPhotos().size(); j++) {
                    if (user.getPhotos().get(j).getPath().equals(pathList.get(i))) {
                        bitmapList.add(user.getPhotos().get(j).bitmap);
                    }
                }
            }
        }

        // get the fields and sets the list view to thumbnails
        listView = findViewById(R.id.literal_photo_list);
        //listView.setAdapter(
        //        new PhotoAdapter(this, pathList));
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, bitmapList));

        currIndex = -1;
    }

    public void movePhoto(View view) {

        //If no Albums, output error
        if (pathList.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Photos to select from");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            currIndex = -1;
            return;
        }

        //If no selected, output error
        if (currIndex < 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Photo selected");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            currIndex = -1;
            return;
        }

        //goes into move photo instance
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        bundle.putInt("INDEX", albIndex);
        bundle.putString("PATH", pathList.get(currIndex));

        Intent intent = new Intent(this, MovePhoto.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, MOVE_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = intent.getExtras();

        String newName;
        //Code that directly executes actions in the database, sans delete
        switch (requestCode) {
            case DISPLAY_PHOTO_CODE:
                user = bundle.getParcelable("USER");
                break;
            case ADD_PHOTO_CODE:
                user = bundle.getParcelable("USER");
                System.out.println("gets to this point");
                Bitmap thumbnail = intent.getParcelableExtra("data");
                System.out.println("gets to this point");
                user.addPhoto(user.getAlbums().get(albIndex).getName(), "asdf", thumbnail);
                break;
            case MOVE_PHOTO_CODE:
                String nAlbName = bundle.getString("NEWNAME");
                String oAlbName = user.getAlbums().get(bundle.getInt("INDEX")).getName();
                String photoPath = bundle.getString("PATH");
                user.movePhoto(oAlbName, nAlbName, photoPath);
                break;

        }

        //gets array of paths
        pathList = new ArrayList<>();
        bitmapList = new ArrayList<>();
        if (user.getConnections() != null) {
            for (int i = 0; i < user.getConnections().size(); i++) {
                //finds connections that have the same album as this one
                String conAlbumName = user.getConnections().get(i).getAlbum();
                String thisAlbumName = user.getAlbums().get(albIndex).getName();
                if (conAlbumName.equals(thisAlbumName)) {
                    pathList.add(user.getConnections().get(i).getPath());

                }
            }
        }
        if (pathList.size() != 0) {
            for (int i = 0; i < pathList.size(); i++) {
                for (int j = 0; j < user.getPhotos().size(); j++) {
                    if (user.getPhotos().get(j).getPath().equals(pathList.get(i))) {
                        bitmapList.add(user.getPhotos().get(j).bitmap);
                    }
                }
            }
        }

        // get the fields and sets the list view to thumbnails
        listView = findViewById(R.id.literal_photo_list);
        //listView.setAdapter(
        //        new PhotoAdapter(this, pathList));
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, bitmapList));
        currIndex = -1;
    }

}
