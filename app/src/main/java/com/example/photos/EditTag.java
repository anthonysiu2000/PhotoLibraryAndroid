package com.example.photos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EditTag extends AppCompatActivity {

    private User user;
    private EditText tagCat, tagVal;
    private Bitmap bitmap;
    public boolean addT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_tag_toolbar);
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
        tagCat = findViewById(R.id.tagCat);
        tagVal = findViewById(R.id.tagValue);

        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("USER");
            addT = bundle.getBoolean("ADD");
            byte[] bytes = bundle.getByteArray("BITMAP");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
    }



    public void confirm(View view) {
        // gather all data from text fields
        String inputCat = tagCat.getText().toString().trim();
        String inputVal = tagVal.getText().toString().trim();

        Tag inputTag = new Tag(inputCat, inputVal);

        if (inputCat == null || inputCat.length() == 0 || inputVal == null || inputVal.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "Category and/or Value cannot be blank");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //gets current photo instance
        ArrayList<Photo> photoList = user.getPhotos();
        int photoIndex = -1;
        for (int i = 0; i < photoList.size(); i++) {
            if (photoList.get(i).bitmap.equals(bitmap)) {
                photoIndex = i;
                break;
            }
        }

        if (addT) {
            if (user.getPhotos().get(photoIndex).addTag(inputTag)) {
                Bundle bundle = new Bundle();
                bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                        "Add Unsuccessful");
                DialogFragment newFragment = new ARAlbumDialog();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return;
            }
        }
        else {
            if (user.getPhotos().get(photoIndex).removeTag(inputTag)) {
                Bundle bundle = new Bundle();
                bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                        "Remove Unsuccessful");
                DialogFragment newFragment = new ARAlbumDialog();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return;
            }

        }

        // make Bundle
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);

        // send back to caller
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish(); // pops activity from the call stack, returns to parent

    }
}
