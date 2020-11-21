package com.ragabaat.admincollage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ragabaat.admincollage.faculty.UpdateFaculty;
import com.ragabaat.admincollage.notice.UploadNotice;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

   private CardView uploadNotice,addGallery,addEbook,faculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadNotice = findViewById(R.id.addNotice);
        addGallery= findViewById(R.id.addGalleryImage);
        addEbook = findViewById(R.id.addEbook);
        faculty = findViewById(R.id.faculty);
        setTitle("");

        uploadNotice.setOnClickListener(this);
        addGallery.setOnClickListener(this);
        addEbook .setOnClickListener(this::onClick);
        faculty .setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()){

            case R.id.addNotice:

                 intent = new Intent(MainActivity.this, UploadNotice.class);
                startActivity(intent);
                break;

            case R.id.addGalleryImage:

                intent = new Intent(MainActivity.this,UploadImage.class);
                startActivity(intent);
                break;

            case R.id.addEbook:

              intent = new Intent(MainActivity.this,UpoladBdfActivity.class);
                startActivity(intent);
                break;

            case R.id.faculty:

                intent = new Intent(MainActivity.this, UpdateFaculty.class);
                startActivity(intent);
                break;
        }

    }
}