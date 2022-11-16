package com.example.kuro.models;

public class EpisodePosition {
    private String epId;
    private long position;
    private String Uid;

    public EpisodePosition(String epId, long position, String uid) {
        this.epId = epId;
        this.position = position;
        Uid = uid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEpId() {
        return epId;
    }

    public void setEpId(String epId) {
        this.epId = epId;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
