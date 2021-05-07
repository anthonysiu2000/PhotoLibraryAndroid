package com.example.photos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PhotoEdit extends AppCompatActivity {
    private ImageView imageView;
    private User user;
    private int albIndex;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_toolbar);
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

        imageView = findViewById(R.id.imageView);

        // see if info was passed in to populate fields
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("USER");
            albIndex = bundle.getInt("INDEX");
            byte[] bytes = bundle.getByteArray("BITMAP");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        if (bitmap != null) {
            System.out.println("its null bro");
        }
        // get the fields and sets the list view to thumbnails
        imageView.setImageBitmap(bitmap);
    }


    public void addTag(View view) {

        //goes into move photo instance
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        bundle.putByteArray("BITMAP", bytes);
        bundle.putBoolean("ADD", true);

        Intent intent = new Intent(this, EditTag.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void deleteTag(View view) {


        //goes into move photo instance
        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        bundle.putByteArray("BITMAP", bytes);
        bundle.putBoolean("ADD", false);

        Intent intent = new Intent(this, EditTag.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void slideshowReverse(View view) {

        //gets array of paths and bitmaps for this album
        ArrayList<String> pathList = new ArrayList<>();
        ArrayList<Bitmap> bitmapList = new ArrayList<>();
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
        if (pathList.size() > 0) {
            for (int i = 0; i < pathList.size(); i++) {
                for (int j = 0; j < user.getPhotos().size(); j++) {
                    if (user.getPhotos().get(j).getPath().equals(pathList.get(i))) {
                        bitmapList.add(user.getPhotos().get(j).bitmap);
                    }
                }
            }
        }

        int bitmapIndex = -1;
        //finds index of the current photo
        for (int j = 0; j < bitmapList.size(); j++) {
            if (bitmap.equals(bitmapList.get(j))) {
                bitmapIndex = j;
                break;
            }
        }

        if (bitmapIndex > 0) {
            bitmap = bitmapList.get(bitmapIndex - 1);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void slideshowForward(View view) {

        //gets array of paths and bitmaps for this album
        ArrayList<String> pathList = new ArrayList<>();
        ArrayList<Bitmap> bitmapList = new ArrayList<>();
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
        if (pathList.size() > 0) {
            for (int i = 0; i < pathList.size(); i++) {
                for (int j = 0; j < user.getPhotos().size(); j++) {
                    if (user.getPhotos().get(j).getPath().equals(pathList.get(i))) {
                        bitmapList.add(user.getPhotos().get(j).bitmap);
                    }
                }
            }
        }

        int bitmapIndex = -1;
        //finds index of the current photo
        for (int j = 0; j < bitmapList.size(); j++) {
            if (bitmap.equals(bitmapList.get(j))) {
                bitmapIndex = j;
                break;
            }
        }

        if (bitmapIndex < bitmapList.size() - 1) {
            bitmap = bitmapList.get(bitmapIndex + 1);
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = intent.getExtras();
        //Code that directly executes actions in the database, sans delete
        user = bundle.getParcelable("USER");
    }
}
