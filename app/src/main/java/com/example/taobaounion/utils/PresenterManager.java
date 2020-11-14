package com.example.taobaounion.utils;

import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.presenter.IOnSellPagePresenter;
import com.example.taobaounion.presenter.ISelectedPagePresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.example.taobaounion.presenter.impl.HomePresenterImpl;
import com.example.taobaounion.presenter.impl.OnSellPagePresenterImp;
import com.example.taobaounion.presenter.impl.SearchPresenter;
import com.example.taobaounion.presenter.impl.SelectedPagePresenterImp;
import com.example.taobaounion.presenter.impl.TickPresenterImp;

/**
 * 使用一个Presenter来管理所有的单例，直接在构造函数中创建，使用饿汉式来加载
 */
public class PresenterManager {
    private static final PresenterManager outInstance = new PresenterManager();
    private final ICategoryPagerPresenter mCategoryPagerPresenter;
    private final ITicketPresenter mTickPresenterImp;
    private final IHomePresenter mHomePresenter;
    private final ISelectedPagePresenter mSelectedPagePresenterImp;
    private final IOnSellPagePresenter mOnSellPagePresenter;
    private final SearchPresenter mSearchPresenter;


    //在这里来管理所有prensenter，在static中持有这些presenter那么就可以不会被gc回收
    private PresenterManager(){
        mCategoryPagerPresenter = new CategoryPagerPresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTickPresenterImp = new TickPresenterImp();
        mSelectedPagePresenterImp = new SelectedPagePresenterImp();
        mOnSellPagePresenter = new OnSellPagePresenterImp();
        mSearchPresenter = new SearchPresenter();
    }

    public ITicketPresenter getTickPresenterImp() {
        return mTickPresenterImp;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public static PresenterManager getInstance(){
        return outInstance;
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return mCategoryPagerPresenter;
    }

    public ISelectedPagePresenter getSelectedPagePresenterImp() {
        return mSelectedPagePresenterImp;
    }

    public IOnSellPagePresenter getOnSellPagePresenter() {
        return mOnSellPagePresenter;
    }

    public SearchPresenter getSearchPresenter() {
        return mSearchPresenter;
    }
}
