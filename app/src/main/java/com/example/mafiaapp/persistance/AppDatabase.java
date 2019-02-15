package com.example.mafiaapp.persistance;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.mafiaapp.persistance.DAO.PlayerDao;
import com.example.mafiaapp.persistance.entity.PlayerEntity;


@Database(entities = PlayerEntity.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlayerDao playerDao();

}
