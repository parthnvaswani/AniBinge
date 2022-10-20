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
    public Float rating;
    @SerializedName("duration")
    public Integer duration;
    @SerializedName("recommendations")
    public List<Anime> recommendations;
    @SerializedName("episodes")
    public List<Episode> episodes;
    @SerializedName("countryOfOrigin")
    public String country;
    @SerializedName("popularity")
    public int popularity;
    @SerializedName("genres")
    public String[] genres;
    @SerializedName("season")
    public String season;
    @SerializedName("studios")
    public String[] studios;
    @SerializedName("type")
    public String type;

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
