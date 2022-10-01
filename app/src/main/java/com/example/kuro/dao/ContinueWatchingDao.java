package com.example.kuro.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.kuro.entities.ContinueWatching;

import java.util.List;

@Dao
public interface ContinueWatchingDao {
    @Query("select * from ContinueWatching")
    LiveData<List<ContinueWatching>> getAll();

    @Insert
    void add(ContinueWatching continueWatching);

    @Query("update ContinueWatching set epNum = :epNum, position = :position where animeId = :animeId")
    void update(String animeId,int epNum,long position);

    @Query("delete from ContinueWatching where animeId = :animeId")
    void deleteByAnimeId(String animeId);

    @Query("select * from ContinueWatching where animeId = :animeId")
    List<ContinueWatching> getByAnimeId(String animeId);
}
