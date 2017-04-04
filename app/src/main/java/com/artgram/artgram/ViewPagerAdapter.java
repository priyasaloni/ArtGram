package com.artgram.artgram;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;

/**
 * Created by sonal on 08-06-2016.
 */
//* Created by sonal on 04-06-2016.


// * Created by hp1 on 21-01-2015.
//*/
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    private ContextCompat resources;

    // Context context = this;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
           TaboneFragment t1 = new TaboneFragment();

            return t1;
        } else if (position == 1) // if the position is 0 we are returning the First tab
        {
            TabtwoFragment t2 = new TabtwoFragment();
            return t2;
        } else if (position == 2)// As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            TabthreeFragment t3 = new TabthreeFragment();
            return t3;
        } else {
            TabfourFragment t4 = new TabfourFragment();
            return t4;
        }
    }
    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public ContextCompat getResources() {
        return resources;
    }


}
