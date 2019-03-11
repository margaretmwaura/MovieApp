package com.example.admin.themovieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;

public class MostPopular extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new TheTopRated())
                    .commit();
        }
    }
}
