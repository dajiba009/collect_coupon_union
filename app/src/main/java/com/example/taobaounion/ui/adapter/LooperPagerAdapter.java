package com.example.taobaounion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % data.size();

        HomePagerContent.DataBean dataBean = data.get(realPosition);
        int measuredHeight = container.getMeasuredHeight();
        int measuredWidth = container.getMeasuredWidth();
        LogUtils.d(this,"measuredHeight ===> " + measuredHeight);
        LogUtils.d(this,"measuredWidth ===> " + measuredWidth);
        int ivSize = (measuredHeight > measuredWidth ? measuredHeight : measuredWidth)/2;
        String coverUrl = UrlUtils.getCoverPath(dataBean.getPict_url(),ivSize);
        ImageView imageView = new ImageView(container.getContext());
        Glide.with(container).load(coverUrl).into(imageView);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public int getDataSize(){
        return data.size();
    }
}
