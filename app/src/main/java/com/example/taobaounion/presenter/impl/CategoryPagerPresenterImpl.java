package com.example.taobaounion.presenter.impl;

import android.util.Log;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    private static CategoryPagerPresenterImpl sInstance = null;

    private Map<Integer,Integer> pagesInfo = new HashMap<>();

    public static final int DEFAULT_PAGE = 1;

    public static CategoryPagerPresenterImpl getInstance(){
        if(sInstance == null){
            synchronized (CategoryPagerPresenterImpl.class){
                if(sInstance == null){
                    sInstance = new CategoryPagerPresenterImpl();
                }
            }
        }
        return sInstance;
    }

    private CategoryPagerPresenterImpl(){

    }

    /**
     * 根据分类id去加载内容
     * @param categoryId
     */
    @Override
    public void getContentByCategoryId(int categoryId) {
        Retrofit retrofit = RetofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Integer targetPage = pagesInfo.get(categoryId);
        if(targetPage == null){
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId,targetPage);
        }
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, 1);
        LogUtils.d(this,"home pager page url ====> " + homePagerUrl);
        Call<HomePagerContent> task = api.getHomePagerContent(homePagerUrl);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this,"code ====> " + code);
                if(code == HttpURLConnection.HTTP_OK){
                    HomePagerContent pageContent = response.body();
                    if(pageContent != null){
                        LogUtils.d(CategoryPagerPresenterImpl.this,"pageContent ===> " + pageContent);
                    }
                }else {
                    //todo:
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this,"onFail ===> " + t.toString());
            }
        });
    }

    @Override
    public void loaderMore(int categoryId) {

    }

    @Override
    public void reload(int categoryId) {

    }

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {

    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {

    }
}
