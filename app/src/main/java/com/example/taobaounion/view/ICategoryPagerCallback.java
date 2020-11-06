package com.example.taobaounion.view;

import com.example.taobaounion.bases.IBaseCallBack;
import com.example.taobaounion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallBack {

    /**
     * 加载数据
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);


    /**
     * 加载更多数据错误
     */
    void onLoadMoreError();

    /**
     *没有内容
     */
    void onLoadMoreEmpty();

    /**
     *加载了更多内容
     * @param contents
     */
    void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     *轮播图内容加载到了
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

    int getCategoryId();
}
