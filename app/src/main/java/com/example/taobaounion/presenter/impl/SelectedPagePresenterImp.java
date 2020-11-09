package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.presenter.ISelectedPagePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ISelectedPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImp implements ISelectedPagePresenter {

    private ISelectedPageCallback mViewCallback = null;
    private final Api mApi;
    private SelectedPageCategory.DataBean mCurrentPageCategoryItem = null;

    public SelectedPagePresenterImp(){
        //拿retrofit
        Retrofit retrofit = RetofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getCategory() {
        if(mViewCallback != null){
            mViewCallback.onLoading();
        }
        Call<SelectedPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int code = response.code();
                LogUtils.d(SelectedPagePresenterImp.class,"result code =====> " + code);
                if(code == HttpURLConnection.HTTP_OK){
                    SelectedPageCategory result = response.body();
                    //通知UI更新
                    if(mViewCallback != null){
                        mViewCallback.onCategoriesLoaded(result);
                    }
                }else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    private void onLoadedError(){
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {
        this.mCurrentPageCategoryItem = item;
        if(mCurrentPageCategoryItem != null){
            Call<SelectedContent> task = mApi.getSelectedPageContent(item.getFavorites_id());
            task.enqueue(new Callback<SelectedContent>() {
                @Override
                public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                    int code = response.code();
                    LogUtils.d(SelectedPagePresenterImp.class,"result code =====> " + code);
                    if(code == HttpURLConnection.HTTP_OK){
                        SelectedContent result = response.body();
                        //通知UI更新
                        LogUtils.d(ISelectedPagePresenter.class,"SelectedContent result =======> " + result.getData().toString());
                        if(mViewCallback != null){
                            mViewCallback.onContentLoaded(result);
                        }
                    }else {
                        onLoadedError();
                    }
                }

                @Override
                public void onFailure(Call<SelectedContent> call, Throwable t) {
                    onLoadedError();
                }
            });
        }
    }

    @Override
    public void reloadContent() {
        if(mCurrentPageCategoryItem != null){
            this.getContentByCategory(mCurrentPageCategoryItem);
        }
    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
        if(mViewCallback != null){
            mViewCallback = null;
        }
    }
}
