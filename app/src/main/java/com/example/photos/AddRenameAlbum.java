package com.example.photos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class AddRenameAlbum extends AppCompatActivity {

    public static final String ALBUM_INDEX = "albumIndex";
    public static final String ALBUM_LIST = "albumList";

    private int albumIndex;
    private ArrayList<Album> albumList;
    private EditText albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rename_album_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // activates the up arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // sets back arrow to send info back to userview
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send back to caller
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        // get the fields
        albumName = findViewById(R.id.album_name);

        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumIndex = bundle.getInt(ALBUM_INDEX);
            albumList = bundle.getParcelableArrayList(ALBUM_LIST);
            if (albumIndex > -1) {
                albumName.setText(albumList.get(albumIndex).getName());
            }
        }
    }



    public void confirm(View view) {
        // gather all data from text fields
        String newName = albumName.getText().toString().trim();

        //makes sure field is not empty
        if (newName == null || newName.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "Album name cannot be empty");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //makes sure album name doesn't already exist
        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getName().equals(newName)) {
                Bundle bundle = new Bundle();
                bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                        "Album name already exists");
                DialogFragment newFragment = new ARAlbumDialog();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return;
            }
        }

        // make Bundle
        Bundle bundle = new Bundle();
        bundle.putString("NEW_NAME", newName);
        bundle.putInt("INDEX", albumIndex);

        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent

    }
}
