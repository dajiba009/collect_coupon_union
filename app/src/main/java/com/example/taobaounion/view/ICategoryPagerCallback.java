package com.example.taobaounion.view;

import com.example.taobaounion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback {

    /**
     * 加载数据
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 加载中
     * @param categoryId
     */
    void onLoading(int categoryId);

    /**
     * 加载错误
     * @param categoryId
     */
    void onError(int categoryId);

    /**
     * 内容为空
     * @param categoryId
     */
    void onEmpty(int categoryId);

    /**
     * 加载更多数据错误
     * @param categoryId
     */
    void onLoadMoreError(int categoryId);

    /**
     *没有内容
     * @param categoryId
     */
    void onLoadMoreEmpty(int categoryId);

    /**
     *加载了更多内容
     * @param contents
     */
    void onLoadMoreLoading(List<HomePagerContent.DataBean> contents);

    /**
     *轮播图内容加载到了
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);
}
