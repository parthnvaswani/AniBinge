package com.example.kuro.models;

public class ContinueWatching {
    private String animeId;
    private String img;
    private int epNum;
    private long position;
    private long updatedAt;
    private String Uid;

    public ContinueWatching() {
    }

    public ContinueWatching(String animeId, String img, int epNum, long position, String uid) {
        this.animeId = animeId;
        this.img = img;
        this.epNum = epNum;
        this.position = position;
        this.updatedAt = System.currentTimeMillis();
        this.Uid = uid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getAnimeId() {
        return animeId;
    }

    public void setAnimeId(String animeId) {
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

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
