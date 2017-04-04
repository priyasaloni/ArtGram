package com.artgram.artgram;

/**
 * Created by sonal on 08-06-2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


/**
 * Created by hp1 on 21-01-2015.
 */
public class tab1 extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

       View v =inflater.inflate(R.layout.activity_main,container,false);


       return v;
    }





}
