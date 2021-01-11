package com.supertridents.removebackground.object.remover;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.util.ArrayList;

public class SavedImagesActivity extends AppCompatActivity {
        GridView gridView;
        ArrayList<File> list;
        private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.saved));

        gridView = findViewById(R.id.image_grid);
        try {
            File f = new File(Environment.getExternalStorageDirectory() + java.io.File.separator + "RemoveMe");
            list = imageReader(f);
        }catch (Exception e){
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle("Alert");
            dialog.setMessage("You Don't have Any Saved Images");
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent home = new Intent(SavedImagesActivity.this,MainActivity.class);
                    startActivity(home);
                    dialog.dismiss();

                }
            });
            dialog.show();
        }

        gridView.setAdapter(new GridAdapter());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(SavedImagesActivity.this,FullImageActivity.class);
                intent.putExtra("img",list.get(i).toString());
                startActivity(intent);
            }
        });




        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-5324429581828078/4119613768");

        mAdView = findViewById(R.id.adViewsavedimages);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
                //Toast.makeText(SavedImagesActivity.this, "ad Loded", Toast.LENGTH_SHORT).show();
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

    public class GridAdapter extends BaseAdapter{

        @Override
        public int getCount(){
            try {
                return list.size();
            }catch (Exception e){
                return 0;
            }
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {

            View converview=null;
            if(converview == null){
                converview=getLayoutInflater().inflate(R.layout.row_layout,parent,false);
                ImageView myimage = converview.findViewById(R.id.myimage);
                myimage.setImageURI(Uri.parse(list.get(i).toString()));

            }
            return converview;
        }
    }
    private ArrayList<File> imageReader(File storageDirectory) {
        ArrayList<File> b= new ArrayList<>();
        File[] file = storageDirectory.listFiles();

        for(int i=0; i<file.length;i++) {
            if(file[i].isDirectory()) {
                b.addAll(imageReader(file[i]));
            }
            else {
                if(file[i].getName().endsWith(".jpg") || file[i].getName().endsWith(".png")){
                  b.add(file[i]);
                }
            }
        }

        return b;
    }
}