package com.example.admin.themovieapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class TheViewPager extends AppCompatActivity {

    FragmentAdapter  adapter;
    private int currentPosition;
    private int previousPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_view_pager);


//        Instantiating the fragments
        Fragment[] fragments = {
                Fragment.instantiate(this, TheTopRated.class.getName()),
                Fragment.instantiate(this, TheMostPopular.class.getName()),
                Fragment.instantiate(this, TheFavourites.class.getName()),

        };



        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.tablayout2);
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.pager);

        // Create an adapter that knows which fragment should be shown on each page
        adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);

        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }
            @Override
            public void onPageSelected(int position)
            {
                previousPosition = currentPosition;
               invalidateOptionsMenu(position);
               currentPosition = position;


             clearing(previousPosition);

            }
            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        invalidateOptionsMenu(0);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout2.setupWithViewPager(viewPager);

//        Removes the actionBar shadow
         this.getSupportActionBar().setElevation(0);
    }

    private void invalidateOptionsMenu(int position)
    {
        for(int i = 0; i < adapter.getCount(); i++)
        {
            Fragment fragment = adapter.getItem(i);
            fragment.setHasOptionsMenu(i == position);

        }

        invalidateOptionsMenu();


    }

    private void clearing(int position)
    {
        if (position == 0)
        {
            TheTopRated theTopRated = new TheTopRated();
            theTopRated.clearingAnimations();
        }

        if(position == 1)
        {
            TheMostPopular theMostPopular = new TheMostPopular();
             theMostPopular.clearingAnimations();
        }
    }





}
