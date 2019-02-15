package com.example.mafiaapp.persistance.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mafiaapp.model.Player;
import com.example.mafiaapp.persistance.entity.PlayerEntity;

import java.util.List;

@Dao
public interface PlayerDao {
    @Query("select * from players")
    List<PlayerEntity> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PlayerEntity playerEntity);

    @Update
    void update(PlayerEntity... entities);

    @Delete
    void delete(PlayerEntity... entities);
}
