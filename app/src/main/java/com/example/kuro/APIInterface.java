package com.example.kuro;

import com.example.kuro.pojo.Anime;
import com.example.kuro.pojo.AnimeInfo;
import com.example.kuro.pojo.Animes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("{search}")
    Call<Animes> searchAnime(@Path("search") String search, @Query("page") int page);

    @GET("random-anime")
    Call<AnimeInfo> randomAnime();

    @GET("popular")
    Call<Animes> popularAnime(@Query("page") int page);

    @GET("trending")
    Call<Animes> trendingAnime(@Query("page") int page);

}
