package com.example.taobaounion.view;

import com.example.taobaounion.bases.IBaseCallBack;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;

import java.util.List;

public interface ISearchViewCallback extends IBaseCallBack {

    /**
     * 搜索历史内容
     * @param histories
     */
    void onHistoriesLoaded(Histories histories);

    /**
     * 历史记录删除完成
     */
    void onHitoriesDeleted();

    /**
     * 搜索结果:成功
     *
     * @param result
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载更多内容
     * @param result
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多时网络出错
     */
    void onMoreLoaderError();

    /**
     * 加载更多内容为空
     */
    void onMoreLoadedEmpty();

    /**
     * 推荐词获取结果
     * @param recommendWords
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);
}
