package com.example.taobaounion.bases;

import com.example.taobaounion.view.IHomeCallback;

public interface IBasePresenter<T> {

    /**
     * 注册ui更新接口
     * @param callback
     */
    void registerViewCallback(T callback);

    /**
     * 取消注册ui更新接口
     * @param callback
     */
    void unregisterViewCallback(T callback);
}
