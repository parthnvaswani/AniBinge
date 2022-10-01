package com.example.kuro.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ContinueWatching")
public class ContinueWatching {
    @PrimaryKey
    @ColumnInfo(name = "animeId")
    @NonNull
    private String animeId;
    @ColumnInfo(name = "img")
    private String img;
    @ColumnInfo(name = "epNum")
    private int epNum;
    @ColumnInfo(name = "position")
    private long position;

    public ContinueWatching(String animeId, String img, int epNum, long position) {
        this.animeId = animeId;
        this.img = img;
        this.epNum = epNum;
        this.position = position;
    }

    @NonNull
    public String getAnimeId() {
        return animeId;
    }

    public void setAnimeId(@NonNull String animeId) {
        this.animeId = animeId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getEpNum() {
        return epNum;
    }

    public void setEpNum(int epNum) {
        this.epNum = epNum;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
