package com.example.kuro;

import android.app.Application;

import com.example.kuro.pojo.AnimeInfo;

public class GlobalState extends Application {
    private AnimeInfo animeInfo;

    public AnimeInfo getAnimeInfo() {
        return animeInfo;
    }

    public void setAnimeInfo(AnimeInfo a) {
        animeInfo = a;
    }
}
