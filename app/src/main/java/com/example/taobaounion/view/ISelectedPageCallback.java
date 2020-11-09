package com.example.taobaounion.view;

import com.example.taobaounion.bases.IBaseCallBack;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;

public interface ISelectedPageCallback extends IBaseCallBack {

    /**
     * 分类内容结果
     *
     * @param result 分类内容
     */
    void onCategoriesLoaded(SelectedPageCategory result);

    /**
     *内容
     *
     * @param content
     */
    void onContentLoaded(SelectedContent content);
}
