package com.example.kuro.pojo;

import com.google.gson.annotations.SerializedName;

public class Anime {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public Title title;
    @SerializedName("image")
    public String image;

    public static class Title{
        @SerializedName("romaji")
        public String romaji;
        @SerializedName("english")
        public String english;
        @SerializedName("native")
        public String nati;
    }
}

