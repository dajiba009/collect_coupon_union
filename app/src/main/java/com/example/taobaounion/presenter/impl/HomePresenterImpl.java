package com.example.taobaounion.presenter.impl;

import android.util.Log;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetofitManager;
import com.example.taobaounion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {
    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        //加载分类数据
        Retrofit retrofit = RetofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this,"code ====> " + code);
                if(code == HttpURLConnection.HTTP_OK){
                    LogUtils.d(this,"请求成功。。。");
                    //请求成功
                    Categories categories = response.body();
                    if (mCallback != null) {
                        if(categories ==null || categories.getData().size() ==0){
                            mCallback.onEmpty();
                        }else {
                            //LogUtils.d(HomePresenterImpl.this,categories.toString());
                            mCallback.onCreategories(categories);
                        }
                    }
                }else {
                    //请求失败
                    LogUtils.i(HomePresenterImpl.this,"请求错误....");
                    if (mCallback != null) {
                        mCallback.onNetWorkError();
                    }
                }

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败的结果
                LogUtils.e(HomePresenterImpl.this,"请求失败.... " + t);
                if (mCallback != null) {
                    mCallback.onNetWorkError();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        mCallback = null;
    }
}
