package com.example.taobaounion.ui.adapter;

import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.ui.fragment.HomePagerFragment;
import com.example.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Categories.DataBean> mCategoriesList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    /**
     * 设置title的
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        LogUtils.d(this,"title ===> " + mCategoriesList.get(position).getTitle());
        return mCategoriesList.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        HomePagerFragment homePagerFragment = HomePagerFragment.newInstance(mCategoriesList.get(position));
        return homePagerFragment;
    }

    @Override
    public int getCount() {
        return mCategoriesList.size();
    }

    /**
     * 获取数据
     * @param categories
     */
    public void setCategories(Categories categories) {
        mCategoriesList.clear();
        List<Categories.DataBean> data = categories.getData();
        mCategoriesList.addAll(data);
        //LogUtils.d(this,"Categories's DataBean size ===> " + mCategoriesList.size());
        notifyDataSetChanged();
    }
}
