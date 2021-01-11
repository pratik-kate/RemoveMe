package com.supertridents.removebackground.object.remover;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroCustomLayoutFragment;
import com.github.appintro.AppIntroFragment;

public class IntroAvtivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addSlide(AppIntroFragment.newInstance("Welcome to Remove It", "Remove Background From Image On Your Fingertips", R.drawable.splashlogo, getResources().getColor(R.color.main)));
        addSlide(AppIntroFragment.newInstance("Select Image to Edit ", "You can select images from device", R.drawable.introselectimage, getResources().getColor(R.color.select)));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_intro_layout1));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_intro_layout2));


        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.creditInfo, MODE_PRIVATE).edit();
        editor.putInt(MainActivity.creditBalance, 10);
        editor.apply();
        editor.commit();


    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}