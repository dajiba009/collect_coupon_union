package com.example.taobaounion.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetofitManager {

    private static RetofitManager sManager = null;
    private final Retrofit mRetrofit;

    public static RetofitManager getInstance(){
        if(sManager == null){
            synchronized (RetofitManager.class){
                if(sManager == null){
                    sManager = new RetofitManager();
                }
            }
        }
        return sManager;
    }

    private RetofitManager(){
        //创建retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
