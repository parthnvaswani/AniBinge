package com.example.kuro.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnimeInfo {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public Anime.Title title;
    @SerializedName("image")
    public String image;
    @SerializedName("description")
    public String desc;
    @SerializedName("trailer")
    public Trailer trailer;
    @SerializedName("status")
    public String status;
    @SerializedName("releaseDate")
    public String year;
    @SerializedName("totalEpisodes")
    public String totalEpisodes;
    @SerializedName("rating")
    public Integer rating;
    @SerializedName("duration")
    public Integer duration;
    @SerializedName("genres")
    public String[] genres;
    @SerializedName("recommendations")
    public List<Anime> recommendations;
    @SerializedName("episodes")
    public List<Episode> episodes;

    public static class Title{
        @SerializedName("romaji")
        public String romaji;
    }

    public static class Trailer{
        @SerializedName("id")
        public String id;
        @SerializedName("thumbnail")
        public String thumbnail;
    }

    public static class Episode{
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("image")
        public String image;
        @SerializedName("description")
        public String desc;
    }
}
