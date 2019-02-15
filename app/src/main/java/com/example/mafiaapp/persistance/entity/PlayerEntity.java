package com.example.mafiaapp.persistance.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "players")
public class PlayerEntity {
    @NonNull
    @PrimaryKey
    public Integer id;
    public String name;
    public Integer reprimand;
}