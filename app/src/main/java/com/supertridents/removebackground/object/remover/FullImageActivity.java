package com.supertridents.removebackground.object.remover;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImageActivity extends AppCompatActivity {

    ImageView fullimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        fullimage = findViewById(R.id.full_image);

        String data = getIntent().getExtras().getString("img");
        fullimage.setImageURI(Uri.parse(data));
    }
}