package com.example.kuro.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.kuro.entities.EpisodePosition;

import java.util.List;

@Dao
public interface EpisodePositionDao {
    @Insert
    void add(EpisodePosition episodePosition);

    @Query("update EpisodePosition set position = :position where epId = :epId")
    void update(String epId,long position);

    @Query("delete from EpisodePosition where epId = :epId")
    void deleteByEpId(String epId);

    @Query("select * from EpisodePosition where epId = :epId")
    List<EpisodePosition> getByEpId(String epId);
}
