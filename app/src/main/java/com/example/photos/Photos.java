package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.graphics.Color;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Photos extends AppCompatActivity {

    private ListView listView;
    private User user;
    private int currIndex = -1;
    private ArrayList<String> albumList;

    public static final int CREATE_ALBUM_CODE = 1;
    public static final int RENAME_ALBUM_CODE = 2;
    public static final int OPEN_ALBUM_CODE = 3;
    public static final int SEARCH_PHOTOS_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list);

        user = new User();
        user.createAlbum("asdf");
        user.createAlbum("asdf");
        user.createAlbum("adf");
        user.createAlbum("df");
        user.createAlbum("f");
        user.createAlbum("af");
        user.createAlbum("a");
        user.createAlbum("s");
        user.createAlbum("d");
        user.createAlbum("asdfas");
        user.createAlbum("assdfsff");
        user.createAlbum("asdxcv");
        user.createAlbum("asdfsfsfsfsf");
        user.createAlbum("ddd");
        user.createAlbum("asdfc");
        user.createAlbum("asdfwer");
        user.createAlbum("asdfq");
        user.createAlbum("asdfe");
        user.createAlbum("asdfw");
        user.createAlbum("aswdf");
        user.createAlbum("asedf");
        user.createAlbum("asdqf");
        albumList = new ArrayList<>();
        for (int i = 0; i < user.getAlbums().size(); i++) {
            albumList.add(user.getAlbums().get(i).getName());
        }
        listView = findViewById(R.id.photos_list);
        listView.setAdapter(
                new ArrayAdapter<String>(this, R.layout.photos, albumList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                currIndex = position;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void openAlbum(View view) {

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
        if (currIndex < 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Album selected");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //goes into open Album instance
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        bundle.putInt("INDEX", currIndex);
        Intent intent = new Intent(this, AlbumView.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, OPEN_ALBUM_CODE);
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
        if (currIndex < 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Album selected");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //goes into add rename instance
        Bundle bundle = new Bundle();
        bundle.putInt(AddRenameAlbum.ALBUM_INDEX, currIndex);
        bundle.putParcelableArrayList(AddRenameAlbum.ALBUM_LIST, user.getAlbums());

        Intent intent = new Intent(this, AddRenameAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, RENAME_ALBUM_CODE);
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
        if (currIndex < 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ARAlbumDialog.MESSAGE_KEY,
                    "No Album Selected");
            DialogFragment newFragment = new ARAlbumDialog();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        //Deletes album from user database
        String albumDeleted = user.getAlbums().get(currIndex).getName();
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

        //redo listview
        albumList = new ArrayList<>();
        for (int i = 0; i < user.getAlbums().size(); i++) {
            albumList.add(user.getAlbums().get(i).getName());
        }
        // redo the adapter to reflect change^K
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, albumList));

        currIndex = -1;
    }

    public void searchPhotos(View view) {
        //goes into search photos instance
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        Intent intent = new Intent(this, SearchPhotos.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SEARCH_PHOTOS_CODE);
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
                break;
            case RENAME_ALBUM_CODE:
                newName = bundle.getString("NEW_NAME");
                int oldIndex = bundle.getInt("INDEX");
                String oldName = user.getAlbums().get(oldIndex).getName();
                user.renameAlbum(oldName, newName);
                break;
            case OPEN_ALBUM_CODE:
                user = bundle.getParcelable("USER");
                break;
            case SEARCH_PHOTOS_CODE:
                user = bundle.getParcelable("USER");
                break;

        }
        System.out.println("got to this point");
        //redo listview
        albumList = new ArrayList<>();
        for (int i = 0; i < user.getAlbums().size(); i++) {
            albumList.add(user.getAlbums().get(i).getName());
        }
        // redo the adapter to reflect change^K
        listView.setAdapter(
                new ArrayAdapter<>(this, R.layout.photos, albumList));
        currIndex = -1;
    }

}