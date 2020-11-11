package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.presenter.IOnSellPagePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPagePresenterImp implements IOnSellPagePresenter {

    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSellPageCallback mOnSellPageCallback = null;
    private final Api mApi;

    public OnSellPagePresenterImp(){
        Retrofit retrofit = RetofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContent() {
        if(mIsLoading){
            return;
        }
        mIsLoading = true;
        if(mOnSellPageCallback != null){
            mOnSellPageCallback.onLoading();
        }
        //获取特惠内容
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        LogUtils.d(this,"targetUrl =====> " + targetUrl );
        Call<OnSellContent> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                LogUtils.d(OnSellPagePresenterImp.class,"code ===> " + code);
                if(code == HttpURLConnection.HTTP_OK){
                    OnSellContent result = response.body();
                    onSuccess(result);
                }else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onError();
            }
        });
    }

    private void onError() {
        mIsLoading = false;
        if(mOnSellPageCallback != null){
            mOnSellPageCallback.onError();
        }
    }

    private void onSuccess(OnSellContent result) {
        if(mOnSellPageCallback != null){
            try {
                if(isEmpty(result)){
                    onEmpty();
                }else {
                    mOnSellPageCallback.onContentLoadedSuccess(result);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean isEmpty(OnSellContent content){
        int size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        return size == 0;
    }

    private void onEmpty() {
        if(mOnSellPageCallback != null){
            mOnSellPageCallback.onEmpty();
        }
    }

    @Override
    public void reload() {
        //重新加载
        this.getOnSellContent();
    }

    /**
     * 当前加载状态
     * 在加载状态的时候不能有其他的状态
     */
    private boolean mIsLoading = false;

    @Override
    public void loaderMore() {
        if(mIsLoading){
            return;
        }
        mIsLoading = true;
        //加载更多
        mCurrentPage++;
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if(code == HttpURLConnection.HTTP_OK){
                    OnSellContent result = response.body();
                    onMoreLoader(result);
                }else {
                    onMoreLoaderError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onMoreLoaderError();
            }
        });
    }

    private void onMoreLoaderError() {
        mIsLoading = false;
        mCurrentPage--;
        if(mOnSellPageCallback != null){
            mOnSellPageCallback.onMoreLoadedError();
        }
    }

    private void onMoreLoader(OnSellContent result) {
        if(mOnSellPageCallback != null){
            if(isEmpty(result)){
                mOnSellPageCallback.onMoreLoadedEmpty();
                mCurrentPage--;
            }else {
                mOnSellPageCallback.onMoreLoaded(result);
            }
        }
    }

    @Override
    public void registerViewCallback(IOnSellPageCallback callback) {
        this.mOnSellPageCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IOnSellPageCallback callback) {

    }
}
