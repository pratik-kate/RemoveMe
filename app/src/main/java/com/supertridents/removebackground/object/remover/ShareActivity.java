package com.supertridents.removebackground.object.remover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.supertridents.removebackground.object.remover.selffie.edit.HoverView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ShareActivity extends AppCompatActivity {
    ImageView back,home,sharebtn,save;
    CardView edit;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        getSupportActionBar().hide();

        back = findViewById(R.id.backbtn);
        home = findViewById(R.id.homebtn);
        sharebtn = findViewById(R.id.sharebtnn);
        edit  = findViewById(R.id.editcard);
        save = findViewById(R.id.saveDevice);


        Intent share = getIntent();
        String directory = share.getStringExtra("dir");
        String fname = share.getStringExtra("file");

        String path = directory+"/"+fname;

        File dest = new File(directory, fname);
        //back.setOnClickListener(v -> ShareActivity.super.onBackPressed());
        back.setOnClickListener(v -> {exitByBackKey();});

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshGallery(dest);
                AlertDialog alert = new AlertDialog.Builder(ShareActivity.this)
                        .setTitle("RemoveMe")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .setMessage("Image Saved To Gallery")
                        .show();


            }
        });
        home.setOnClickListener(v -> {
            Intent home = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(home);
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getApplicationContext(),CropActivity.class);
                startActivity(edit);
            }
        });

        sharebtn.setOnClickListener(v -> {


            Intent intent = new Intent(Intent.ACTION_SEND);

            Uri screenshotUri = Uri.parse(path);
            try {
                InputStream stream = getContentResolver().openInputStream(screenshotUri);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "SHARE APP USING"));
        });

        File imgFile = new  File(path);
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.shareimage);
            myImage.setImageBitmap(myBitmap);
        };

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.SMART_BANNER);

        adView.setAdUnitId("ca-app-pub-5324429581828078/7867287086");

        mAdView = findViewById(R.id.adViewshare);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
               // Toast.makeText(ShareActivity.this, "ad Loded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to go back?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    finish();
                })
                .setNegativeButton("No", (arg0, arg1) -> arg0.dismiss())
                .show();
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        getApplicationContext().sendBroadcast(mediaScanIntent);
    }
}

