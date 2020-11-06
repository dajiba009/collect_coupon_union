package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetofitManager;
import com.example.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    private static CategoryPagerPresenterImpl sInstance = null;

    private Map<Integer,Integer> pagesInfo = new HashMap<>();

    public static final int DEFAULT_PAGE = 1;

    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();
    private Integer mCurrenterPage;

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
        for (ICategoryPagerCallback callback : callbacks) {
            if(callback.getCategoryId() == categoryId){
                callback.onLoading();
            }
        }
        Integer targetPage = pagesInfo.get(categoryId);
        Call<HomePagerContent> task = createTask(categoryId,targetPage);
//        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, 1);
//        LogUtils.d(this,"home pager page url ====> " + homePagerUrl);
//        Call<HomePagerContent> task = api.getHomePagerContent(homePagerUrl);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this,"code ====> " + code);
                if(code == HttpURLConnection.HTTP_OK){
                    HomePagerContent pageContent = response.body();
                    if(pageContent != null){
                        LogUtils.d(CategoryPagerPresenterImpl.this,"pageContent ===> " + pageContent.getData().toString());
                        //把加载到的内容更新到UI层
                        handleHomePageContentResult(pageContent,categoryId);
                    }
                }else {
                    handleNetWorkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this,"onFail ===> " + t.toString());
                handleNetWorkError(categoryId);
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        Retrofit retrofit = RetofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        if(targetPage == null){
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId,targetPage);
        }
        return api.getHomePagerContentTwo(categoryId,targetPage);
    }

    private void handleNetWorkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if(callback.getCategoryId() == categoryId){
                callback.onError();
            }
        }
    }

    private void handleHomePageContentResult(HomePagerContent pageContent, int categoryId) {
        //通知UI层更新数据
        List<HomePagerContent.DataBean> data = pageContent.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if(callback.getCategoryId() == categoryId){
                if(pageContent == null || pageContent.getData().size() == 0){
                    callback.onEmpty();
                }else {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {
        //加载更多数据
        //获取当前的页码
        mCurrenterPage = pagesInfo.get(categoryId);
        //因为是加载更多所以页码加一
        if(mCurrenterPage == null){
            mCurrenterPage = 1;
        }
        mCurrenterPage++;
        pagesInfo.put(categoryId,mCurrenterPage);
        //获取数据
        Call<HomePagerContent> task = createTask(categoryId, mCurrenterPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                if(code == HttpURLConnection.HTTP_OK){
                    HomePagerContent result = response.body();
                    handleLoaderResult(result,categoryId);
                }else {
                    handleLoaderModeError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                //请求失败
                LogUtils.d(CategoryPagerPresenterImpl.class,"Error : " + t.toString());
                handleLoaderModeError(categoryId);
            }
        });


    }

    private void handleLoaderResult(HomePagerContent result, int categoryId) {
        //通知UI层更新数据
        List<HomePagerContent.DataBean> data = result.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if(callback.getCategoryId() == categoryId){
                if(result == null || result.getData().size() == 0){
                    callback.onLoadMoreEmpty();
                }else {
                    callback.onLoadMoreLoaded(data);
                }
            }
        }
    }

    private void handleLoaderModeError(int categoryId) {
        mCurrenterPage--;
        pagesInfo.put(categoryId,mCurrenterPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if(categoryId == callback.getCategoryId()){
                callback.onLoadMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if(!callbacks.contains(callback)){
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }
}
