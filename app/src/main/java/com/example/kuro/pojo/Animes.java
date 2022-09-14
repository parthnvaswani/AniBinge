package com.example.kuro.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Animes {
    @SerializedName("currentPage")
    public Integer page;
    @SerializedName("hasNextPage")
    public Boolean hasNextPage;
    @SerializedName("results")
    public List<Anime> results = null;
}
