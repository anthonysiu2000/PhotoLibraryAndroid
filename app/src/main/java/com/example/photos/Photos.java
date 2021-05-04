package com.example.photos;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Photos extends AppCompatActivity {

    private ListView listView;
    private User user;

    public static final int CREATE_ALBUM_CODE = 1;
    public static final int RENAME_MOVIE_CODE = 2;
    public static final int OPEN_ALBUM_CODE = 3;
    public static final int SEARCH_PHOTOS_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list);

        user = new User();


        listView = findViewById(R.id.photos_list);
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, user.getAlbums()));
    }


    public void openAlbum(View view) {
        int index = listView.getSelectedItemPosition();

        //switches scene to Album view, given the currently selected album
        FXMLLoader albumViewLoader = new FXMLLoader();
        albumViewLoader.setLocation(getClass().getResource("/view/AlbumView.fxml"));
        AnchorPane albumViewRoot = (AnchorPane)albumViewLoader.load();
        Scene albumViewScene = new Scene(albumViewRoot);
        AlbumViewController albumViewController = albumViewLoader.getController();

        albumViewController.start(stage, thisScene, albumViewScene, nonAdmin, index);
        stage.setScene(albumViewScene);
        stage.setTitle("AlbumView");
        stage.setResizable(false);
        stage.show();

    }

    public void createAlbum(View view) {

        //goes into add rename instance
        Bundle bundle = new Bundle();
        bundle.putInt(AddRenameAlbum.ALBUM_INDEX, -1);
        bundle.putParcelableArrayList(AddRenameAlbum.ALBUM_LIST, user.getAlbums());

        Intent intent = new Intent(this, AddRenameAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CREATE_ALBUM_CODE);
    }

    public void renameAlbum(View view) {

        //If no Albums, output error
        if (user.getAlbums().size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Albums to select from");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //If no selected, output error
        int index = listView.getSelectedItemPosition();
        if (index < 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Albums to select from");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //goes into add rename instance
        Bundle bundle = new Bundle();
        bundle.putInt(AddRenameAlbum.ALBUM_INDEX, index);
        bundle.putParcelableArrayList(AddRenameAlbum.ALBUM_LIST, user.getAlbums());

        Intent intent = new Intent(this, AddRenameAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, RENAME_MOVIE_CODE);
    }

    public void deleteAlbum(View view) {

        //If no Albums, output error
        if (user.getAlbums().size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Albums to select from");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //If no selected, output error
        int index = listView.getSelectedItemPosition();
        if (index < 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Album Selected");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //Deletes album from user database
        String albumDeleted = user.getAlbums().get(index).getName();
        boolean deleted = user.deleteAlbum(albumDeleted);
        if (!deleted) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "Album Not Deleted Properly");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        // redo the adapter to reflect change^K
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, user.getAlbums()));


        if (user.getAlbums().size() == 0) {
            return;
        }
        else if (index < user.getAlbums().size()) {
            listView.setSelection(index);
        }
        else {
            listView.setSelection(index - 1);
        }
    }

    public void searchPhotos(View view) {
        //goes into search photos instance
        //Bundle bundle = new Bundle();
        //bundle.putParcelable("USER", user);
        //Intent intent = new Intent(this, SearchPhotos.class);
        //intent.putExtras(bundle);
        //startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        String newName;
        //Code that directly executes actions in the database, sans delete
        switch (requestCode) {
            case CREATE_ALBUM_CODE:
                newName = bundle.getString("NEW_NAME");
                user.createAlbum(newName);
            case RENAME_MOVIE_CODE:
                newName = bundle.getString("NEW_NAME");
                int oldIndex = bundle.getInt("INDEX");
                String oldName = user.getAlbums().get(oldIndex).getName();
                user.renameAlbum(oldName, newName);
            case OPEN_ALBUM_CODE:

            case SEARCH_PHOTOS_CODE:

        }

        // redo the adapter to reflect change^K
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, user.getAlbums()));
    }

}