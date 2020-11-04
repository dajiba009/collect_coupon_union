package com.example.taobaounion.bases;

/**
 * 因为所有与网路相关的都要有下面三个相关的，所以就抽取出来
 */
public interface IBaseCallBack {
    void onNetWorkError();

    void onLoading();

    void onEmpty();
}
