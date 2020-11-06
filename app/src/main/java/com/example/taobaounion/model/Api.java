package com.example.taobaounion.model;

import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePagerContent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @GET("discovery/{materialId}/{page}")
    Call<HomePagerContent> getHomePagerContentTwo(@Path("materialId") int materialId,@Path("page") int page);
}
