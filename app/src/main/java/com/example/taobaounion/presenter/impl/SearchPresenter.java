package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.ISearchPresenter;
import com.example.taobaounion.utils.JsonCacheUtil;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetofitManager;
import com.example.taobaounion.view.ISearchViewCallback;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenter implements ISearchPresenter {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_HISTORIES_SIZE = 10;
    private int historiesMaxSize = DEFAULT_HISTORIES_SIZE;

    private final Api mApi;
    private ISearchViewCallback mSearchViewCallback = null;
    private String mCurrentKeyWord = null;
    private final JsonCacheUtil mJsonCacheUtil;

    public SearchPresenter() {
        Retrofit retrofit = RetofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }

    public int mCurrentPage = DEFAULT_PAGE;

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if(mSearchViewCallback != null){
            mSearchViewCallback.onHistoriesLoaded(histories);
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtil.deleteCache(KEY_HISTORIES);
        if(mSearchViewCallback != null){
            mSearchViewCallback.onHitoriesDeleted();
        }
    }

    public static final String KEY_HISTORIES = "key_histories";

    /**
     * 添加历史记录
     */
    private void saveHistory(String history){
//        Type type = new TypeToken<List<String>>() {
//        }.getType();
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        //如果不存在，就干掉,然后再添加
        List<String> historiesList = null;
        if(histories != null && histories.getHistories()!=null){
            historiesList = histories.getHistories();
            if(historiesList.contains(history)){
                historiesList.remove(history);
            }
        }
        //去重完成
        //处理没有处理的数据
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if(histories == null){
            histories = new Histories();
        }

        histories.setHistories(historiesList);
        //对个数进行限制
        if(historiesList.size() > historiesMaxSize){
            historiesList = historiesList.subList(0,historiesMaxSize);
        }
        //添加记录
        historiesList.add(history);
        //保存记录
        mJsonCacheUtil.saveCache(KEY_HISTORIES,histories);
    }

    @Override
    public void research() {
        if(mCurrentKeyWord == null){
            if(mSearchViewCallback != null){
                mSearchViewCallback.onEmpty();
            }
        }else {
            //可以重新搜索
            this.doSearch(mCurrentKeyWord);
        }
    }

    @Override
    public void doSearch(String keyword) {
        if(mCurrentKeyWord == null || !mCurrentKeyWord.endsWith(keyword)){
            this.saveHistory(keyword);
            this.mCurrentKeyWord = keyword;
        }
        //更新UI状态
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.class, "code ===> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onError();
            }
        });
    }

    private void onError() {
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onError();
        }
    }

    private void handleSearchResult(SearchResult result) {
        if (mSearchViewCallback != null) {
            if (isResultEmpty(result)) {
                //数据为空
                mSearchViewCallback.onEmpty();
            }else {
                mSearchViewCallback.onSearchSuccess(result);
            }
        }
    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void loaderMore() {
        mCurrentPage++;
        //进行搜索
        if(mCurrentKeyWord == null){
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        }else {
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.class, "code ===> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleMoreSearchResult(response.body());
                } else {
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoaderMoreError();
            }
        });
    }

    /**
     * 处理加载更多的结果
     * @param result
     */
    private void handleMoreSearchResult(SearchResult result) {
        if (mSearchViewCallback != null) {
            if (isResultEmpty(result)) {
                //数据为空
                mSearchViewCallback.onMoreLoaderError();
            }else {
                mSearchViewCallback.onMoreLoaded(result);
            }
        }
    }

    /**
     * 加载更多内容失败
     */
    private void onLoaderMoreError() {
        mCurrentPage--;
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onMoreLoaderError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.class, "code ===> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //处理结果
                    if (mSearchViewCallback != null) {
                        mSearchViewCallback.onRecommendWordsLoaded(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchViewCallback callback) {
        this.mSearchViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchViewCallback callback) {
        this.mSearchViewCallback = null;
    }
}
