package com.example.kuro.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "EpisodePosition")
public class EpisodePosition {
    @PrimaryKey
    @ColumnInfo(name = "epId")
    @NonNull
    private String epId;
    @ColumnInfo(name = "position")
    private long position;

    public EpisodePosition(@NonNull String epId, long position) {
        this.epId = epId;
        this.position = position;
    }

    @NonNull
    public String getEpId() {
        return epId;
    }

    public void setEpId(@NonNull String epId) {
        this.epId = epId;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
