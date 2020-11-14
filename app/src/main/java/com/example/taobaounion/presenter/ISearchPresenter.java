package com.example.taobaounion.presenter;

import com.example.taobaounion.bases.IBasePresenter;
import com.example.taobaounion.view.ISearchViewCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchViewCallback> {

    /**
     *获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 重新搜索
     */
    void research();

    /**
     * 搜索
     * @param keyword
     */
    public void doSearch(String keyword);

    /**
     * 获取更多的搜索结果
     */
    void loaderMore();

    /**
     * 获取推荐词
     */
    void getRecommendWords();
}
