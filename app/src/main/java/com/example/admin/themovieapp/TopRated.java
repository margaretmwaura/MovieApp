package com.example.admin.themovieapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class TopRated extends AppCompatActivity
{
    public static TopRated activity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = this;
        setContentView(R.layout.category);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new TheTopRated())
                    .commit();
        }
    }

    public static TopRated getInstance()
    {
        return activity;
    }



}
