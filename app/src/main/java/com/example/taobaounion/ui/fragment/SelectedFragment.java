package com.example.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseFragment;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.presenter.ISelectedPagePresenter;
import com.example.taobaounion.ui.adapter.SelectPageContentAdapter;
import com.example.taobaounion.ui.adapter.SelectedPageLeftAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.view.ISelectedPageCallback;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener {

    @BindView(R.id.left_category_list)
    public RecyclerView leftRecycle;

    @BindView(R.id.content_list)
    public RecyclerView rightRecycle;

    private ISelectedPagePresenter mSelectedPagePresenterImp;
    private SelectedPageLeftAdapter mAdapter;
    private SelectPageContentAdapter mContentAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.framgent_selected;
    }

    @Override
    protected void initView(View view) {
        setUpStates(State.SUCCESS);
        leftRecycle.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new SelectedPageLeftAdapter();
        leftRecycle.setAdapter(mAdapter);
        rightRecycle.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mContentAdapter = new SelectPageContentAdapter();
        rightRecycle.setAdapter(mContentAdapter);
        rightRecycle.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dip2px(getContext(), 4);
                int leftAndRight = SizeUtils.dip2px(getContext(), 8);
                outRect.top = topAndBottom;
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
                outRect.bottom = topAndBottom;
            }
        });

    }

    @Override
    protected void initPresenter() {
        mSelectedPagePresenterImp = PresenterManager.getInstance().getSelectedPagePresenterImp();
        mSelectedPagePresenterImp.registerViewCallback(this);
        mSelectedPagePresenterImp.getCategory();

    }

    @Override
    protected void initListener() {
        mAdapter.setOnLeftItemClickListener(this);
    }

    @Override
    protected void release() {
        mSelectedPagePresenterImp.unregisterViewCallback(this);
    }

    //=============================================callback============================
    @Override
    public void onCategoriesLoaded(SelectedPageCategory result) {
        setUpStates(State.SUCCESS);
        //分类内容
        //更新UI
        mAdapter.setData(result);
        //根据当前选中的分类，分类详情内容
        List<SelectedPageCategory.DataBean> data = result.getData();
        mSelectedPagePresenterImp.getContentByCategory(data.get(0));

    }

    @Override
    public void onContentLoaded(SelectedContent content) {
//        LogUtils.d(this,"onContentLoaded =====> " + content.getData()
//                .getTbk_dg_optimus_material_response()
//                .getResult_list()
//                .getMap_data()
//                .get(0)
//                .getTitle());
        mContentAdapter.setData(content);
        rightRecycle.scrollToPosition(0);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {
        setUpStates(State.LOADING);
    }

    @Override
    public void onEmpty() {

    }

    //=============================================callback============================

    //===============================================适配器callback=====================
    @Override
    public void onLeftItemClick(SelectedPageCategory.DataBean item) {
        //左边的点击
        mSelectedPagePresenterImp.getContentByCategory(item);
        LogUtils.d(this,"点击了：" + item.getFavorites_title());
    }
    //===============================================适配器callback=====================
}
