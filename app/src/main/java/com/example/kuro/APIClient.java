package com.example.kuro;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://consumet-api.herokuapp.com/meta/anilist/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}
