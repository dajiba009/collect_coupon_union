package com.example.taobaounion.view;

import com.example.taobaounion.bases.IBaseCallBack;
import com.example.taobaounion.model.domain.Categories;

/**
 * 实现具体的方法是由presenter实现
 * 而IHomeCallback之所以在view包中，是这个接口是用来更新ui的，必须由主线程中的类(在主线程中有用到的类的对象)来实现这个接口
 * presenter实现了方法后，通过回调来进而更新UI，所以presenter的registerCallBack是IHomeCallback
 */
public interface IHomeCallback extends IBaseCallBack {

    void onCreategories(Categories categories);

}
