package com.supertridents.removebackground.object.remover;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    public static String creditInfo ="creditinfo";
    public static String creditBalance ="creditbalance";
    CardView selectimage,savedimage,settings,credits;
    private AdView mAdView;
    TextView credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        credit = findViewById(R.id.credits);
        selectimage = findViewById(R.id.selectImage);
        savedimage = findViewById(R.id.savedImages);
        settings = findViewById(R.id.settings);
        credits = findViewById(R.id.buyCredits);

        SharedPreferences preferences = getSharedPreferences(creditInfo,MODE_PRIVATE);
        credit.setText(String.valueOf(preferences.getInt(creditBalance,0)));

        final int[] credit ={preferences.getInt(MainActivity.creditBalance,0)};
        selectimage.setOnClickListener(v -> {

            if(credit[0]<1){
                    AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("You Don't Have Enough Credits")
                            .setPositiveButton("Buy Credits", (arg0, arg1) -> {
                                Intent intent = new Intent(getApplicationContext(),CreditsActivity.class);
                                startActivity(intent);

                            })
                            .setNegativeButton("Cancel", (arg0, arg1) -> {

                            })
                            .show();
                }
            else{
                Intent intent = new Intent(getApplicationContext(),CropActivity.class);
                startActivity(intent);
            }
        });

        savedimage.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),SavedImagesActivity.class);
            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
        });

        credits.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),CreditsActivity.class);
            startActivity(intent);
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-5324429581828078/6070672454");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
                //Toast.makeText(MainActivity.this, "ad Loded", Toast.LENGTH_SHORT).show();
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



        //Setting Credits




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
                .setMessage("Do you want to exit the application?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    finish();
                })
                .setNegativeButton("No", (arg0, arg1) -> arg0.dismiss())
                .show();
    }

}