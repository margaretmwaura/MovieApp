package com.example.admin.themovieapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentAdapter extends FragmentStatePagerAdapter
{

    private final Fragment[] fragments;
    private String tabTitles[] = new String[] { "Top Rated", "The Popular","Favourites" };
    public FragmentAdapter (FragmentManager fm,Fragment[] fragments)
    {
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int position)
    {
        if (position == 0)
        {
            return new TheTopRated();
        }
        if(position == 1 )
        {
            return new TheMostPopular();
        }
        else
        {
            return new TheFavourites();
        }

    }

    @Override
    public int getCount()
    {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position)
    {
        // Generate title based on item position

        return tabTitles[position];
    }
}
