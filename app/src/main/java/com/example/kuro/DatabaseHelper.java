package com.example.kuro;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.kuro.dao.ContinueWatchingDao;
import com.example.kuro.entities.ContinueWatching;

@Database(entities = ContinueWatching.class,exportSchema = false,version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    private static final String DB_NAME="anibingedb";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDB(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context, DatabaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract ContinueWatchingDao continueWatchingDao();
}
