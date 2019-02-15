package com.example.mafiaapp.persistance;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DbWrapper {
    private static AppDatabase sInstance = null;

    public static void create(Context context) {        //creating Database
        sInstance = Room.databaseBuilder(context,
                AppDatabase.class, "players")
                .build();
    }

    public static AppDatabase getAppDatabase() {
        if (sInstance == null) {
            throw new IllegalStateException("Database not created");
        }

        return sInstance;
    }
}