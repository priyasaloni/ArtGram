package com.artgram.artgram;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by sonal on 03-04-2017.
 */
public class Artgram extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
