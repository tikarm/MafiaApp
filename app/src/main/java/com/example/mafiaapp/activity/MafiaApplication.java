package com.example.mafiaapp.activity;

import android.app.Application;

import com.example.mafiaapp.persistance.DbWrapper;

public class MafiaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DbWrapper.create(this);
    }
}