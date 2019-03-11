package com.example.admin.themovieapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {Movie.class},version = 1,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase
{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "MovieList";
    private static MovieDatabase movieDatabase;

    public static MovieDatabase getInstance(Context context)
    {

        if(movieDatabase == null)
        {
            synchronized (LOCK)
            {
                movieDatabase = Room.databaseBuilder(context.getApplicationContext(),MovieDatabase.class,movieDatabase.DATABASE_NAME)
                        .build();
                Log.d("RoomDatabase" , "Database instance created");
            }
        }

        Log.d("RoomDatabase","Database Instance gotten");
        return movieDatabase;
    }
    public abstract MovieDao movieDao();
}
