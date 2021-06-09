package com.eflexsoft.larrysnews.api;

import com.eflexsoft.larrysnews.model.News;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country,
            @Query("apiKey") String apikey
    );

    @GET("top-headlines")
    Call<News> getNewsWithCategory(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apikey
    );

    @GET("everything")
    Call<News> searchNews(@QueryMap Map<String,String> searchMap);

}
