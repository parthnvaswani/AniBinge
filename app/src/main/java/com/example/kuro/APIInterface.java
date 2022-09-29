package com.example.kuro;

import com.example.kuro.pojo.Anime;
import com.example.kuro.pojo.AnimeInfo;
import com.example.kuro.pojo.Animes;
import com.example.kuro.pojo.EpisodeLinks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("{search}")
    Call<Animes> searchAnime(@Path("search") String search, @Query("page") int page);

    @Headers("Cache-Control: no-cache")
    @GET("random-anime")
    Call<AnimeInfo> randomAnime();

    @GET("popular")
    Call<Animes> popularAnime(@Query("page") int page);

    @GET("trending")
    Call<Animes> trendingAnime(@Query("page") int page);

    @GET("info/{id}")
    Call<AnimeInfo> animeInfo(@Path("id") String id,@Query("dub") boolean subOrDub);

    @GET("watch/{id}")
    Call<EpisodeLinks> getEpisodeLinks(@Path("id") String id);

}
