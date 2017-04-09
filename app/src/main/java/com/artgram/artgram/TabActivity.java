package com.artgram.artgram;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;


        import android.os.Bundle;
        import android.support.design.widget.TabLayout;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.artgram.artgram.R;
import com.artgram.artgram.ViewPagerAdapter;

/**
 * Created by sonal on 04-04-2017.
 */
public class TabActivity extends AppCompatActivity {
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private TabLayout mTabLayout;
    private String[] Titles = {"HOME", "ARTBOARD", "MYPROFILE", "MYSAVED"};
    private int Numboftabs = 4;
    private FloatingActionButton galleryUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
        pager = (ViewPager) findViewById(R.id.pager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
        galleryUpload=(FloatingActionButton)findViewById(R.id.addphoto);
        galleryUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(TabActivity.this,PhotoUpload.class));
            }
        });
        // Assigning ViewPager View and setting the adapter
        pager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(pager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        for (int i = 0; i < 4; i++) {
            mTabLayout.getTabAt(i).setText(Titles[i]);

        }
    }
}
