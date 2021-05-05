package com.example.photos;

import android.content.Intent;
import android.graphics.Bitmap;
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

public class SearchPhotos extends AppCompatActivity {
    private ListView listView;
    private User user;
    private int currIndex = -1;
    private ArrayList<String> pathList;

    public static final int ONE_TAG_CODE = 1;
    public static final int TWO_TAG_OR_CODE = 2;
    public static final int TWO_TAG_AND_CODE = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_photos_toolbar);
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
        }

        //gets array of paths
        pathList = new ArrayList<>();
        if (user.getPhotos() != null) {
            for (int i = 0; i < user.getPhotos().size(); i++) {
                pathList.add(user.getConnections().get(i).getPath());
            }
        }

        // get the fields and sets the list view to thumbnails
        listView = findViewById(R.id.literal_photo_list);
        //listView.setAdapter(
        //        new PhotoAdapter(this, pathList));
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, pathList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                currIndex = position;
            }
        });
    }

    public void oneTag(View view) {

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

        //goes into tag search instance
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        Intent intent = new Intent(this, PhotoEdit.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ONE_TAG_CODE);
    }


    public void twoTagOr(View view) {
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

        //goes into tag search instance
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        Intent intent = new Intent(this, PhotoEdit.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, TWO_TAG_OR_CODE);
        currIndex = -1;
    }

    public void twoTagAnd(View view) {
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

        //goes into tag search instance
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        Intent intent = new Intent(this, PhotoEdit.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, TWO_TAG_AND_CODE);
        currIndex = -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        ArrayList<Photo> photoList = null;
        Tag tag1 = null;
        Tag tag2 = null;
        String newName;
        //Code that directly executes actions in the database, sans delete
        switch (requestCode) {
            case ONE_TAG_CODE:
                tag1 = bundle.getParcelable("TAG1");
                photoList = user.searchPhotoByTag(tag1, null, true);
                break;
            case TWO_TAG_OR_CODE:
                tag1 = bundle.getParcelable("TAG1");
                tag2 = bundle.getParcelable("TAG2");
                photoList = user.searchPhotoByTag(tag1, tag2, false);
                break;
            case TWO_TAG_AND_CODE:
                tag1 = bundle.getParcelable("TAG1");
                tag2 = bundle.getParcelable("TAG2");
                photoList = user.searchPhotoByTag(tag1, tag2, true);
                break;

        }

        //redo listview
        pathList = new ArrayList<>();
        if (photoList != null) {
            for (int i = 0; i < photoList.size(); i++) {
                pathList.add(photoList.get(i).getPath());
            }
        }

        // redo the adapter to reflect change^K
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, pathList));
        currIndex = -1;
    }


}
