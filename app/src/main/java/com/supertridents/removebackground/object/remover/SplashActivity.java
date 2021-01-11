package com.supertridents.removebackground.object.remover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
                boolean first = pref.getBoolean("first",true);
                Intent intent;
                if(first)
                {
                    intent = new Intent(getApplicationContext(), IntroAvtivity.class);
                    SharedPreferences prefs = getSharedPreferences("pref",MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("first",false);
                    editor.apply();
                }
                else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },3000);

        Constants.loadRewardedAd(this);
    }
}