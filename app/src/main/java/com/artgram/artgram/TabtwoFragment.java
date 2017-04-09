package com.artgram.artgram;

/**
 * Created by sonal on 08-06-2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.client.Firebase;


/**
 * Created by hp1 on 21-01-2015.
 */
public class TabtwoFragment extends Fragment {

private Button gallery_button;
private StorageManager storage;
    public static final int GALLERY_INTENT=2;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        View v =inflater.inflate(R.layout.tabtwo,container,false);
      gallery_button= (Button) v.findViewById(R.id.galleryButton);

        return v;
    }





}
