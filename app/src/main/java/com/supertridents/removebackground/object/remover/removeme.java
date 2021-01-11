package com.supertridents.removebackground.object.remover;

import android.app.Application;


public class removeme extends Application {

    private int credits = 5;

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
