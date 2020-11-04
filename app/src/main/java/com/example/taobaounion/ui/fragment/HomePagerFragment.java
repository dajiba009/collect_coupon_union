package com.example.taobaounion.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.view.ICategoryPagerCallback;

import java.util.List;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private CategoryPagerPresenterImpl mCategoryPagerPresenter;

    public static HomePagerFragment newInstance(Categories.DataBean dataBean){
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE,dataBean.getTitle());
        bundle.putInt(Constants.KEY_HOME_MATERIAL_ID,dataBean.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void initView(View view) {
        setUpStates(State.SUCCESS);
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = CategoryPagerPresenterImpl.getInstance();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String tilte = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        int materialId = arguments.getInt(Constants.KEY_HOME_MATERIAL_ID);
        LogUtils.d(this,"title ===> " + tilte);
        LogUtils.d(this,"materialId ====> " + materialId);
        //todo:填充数据
        if(mCategoryPagerPresenter != null){
            mCategoryPagerPresenter.getContentByCategoryId(materialId);
        }

    }

    @Override
    protected void release() {
        if(mCategoryPagerPresenter != null){
            mCategoryPagerPresenter.unregisterViewCallback(this);
        }
    }

    //===============================================================CallBack======================================
    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLoading(int categoryId) {

    }

    @Override
    public void onError(int categoryId) {

    }

    @Override
    public void onEmpty(int categoryId) {

    }

    @Override
    public void onLoadMoreError(int categoryId) {

    }

    @Override
    public void onLoadMoreEmpty(int categoryId) {

    }

    @Override
    public void onLoadMoreLoading(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {

    }
    //===============================================================CallBack======================================
}
