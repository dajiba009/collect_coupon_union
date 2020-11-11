package com.example.taobaounion.view;

import com.example.taobaounion.bases.IBaseCallBack;
import com.example.taobaounion.model.domain.OnSellContent;

public interface IOnSellPageCallback extends IBaseCallBack {

    /**
     * 加载特惠内容
     * @param result
     */
    void onContentLoadedSuccess(OnSellContent result);

    /**
     *加载更多
     * @param moreResult
     */
    void onMoreLoaded(OnSellContent moreResult);

    /**
     *加载更多错误
     */
    void onMoreLoadedError();

    /**
     * 没有更多内容为空
     */
    void onMoreLoadedEmpty();
}
