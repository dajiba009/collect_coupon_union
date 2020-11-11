package com.example.taobaounion.presenter;

import com.example.taobaounion.bases.IBasePresenter;
import com.example.taobaounion.view.IOnSellPageCallback;

public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPageCallback> {
    /**
     * 加载特惠内容
     */
    void getOnSellContent();

    /**
     * 重新加载内容
     *
     * @Callback 网络加载出问题,恢复网络以后
     */
    void reload();

    /**
     * 加载更多特惠内容
     */
    void loaderMore();
}
