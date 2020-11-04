package com.example.taobaounion.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;

public class HomePagerFragment extends BaseFragment {

    public static HomePagerFragment newInstance(Categories.DataBean dataBean){
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE,dataBean.getFavorites_title());
        bundle.putInt(Constants.KEY_HOME_MATERIAL_ID,dataBean.getFavorites_id());
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
    protected void loadData() {
        Bundle arguments = getArguments();
        String tilte = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        int materialId = arguments.getInt(Constants.KEY_HOME_MATERIAL_ID);
        //todo:填充数据
        LogUtils.d(HomePagerFragment.class,"title ===> " + tilte + " materialId ====> " + materialId);

    }
}
