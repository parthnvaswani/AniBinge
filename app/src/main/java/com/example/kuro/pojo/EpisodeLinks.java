package com.example.kuro.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EpisodeLinks {
    @SerializedName("sources")
    public List<Source> sources;

    public static class Source {
        @SerializedName("url")
        public String url;
    }
}
