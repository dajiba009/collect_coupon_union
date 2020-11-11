package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseFragment;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.presenter.IOnSellPagePresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.ui.activiry.TicketActivity;
import com.example.taobaounion.ui.adapter.OnSellContentAdapter;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellPageCallback, OnSellContentAdapter.OnSellContentItmeClickListener {

    public static final int DEFALUT_SPAN_COUNT = 2;

    private IOnSellPagePresenter mOnSellPagePresenter;

    @BindView(R.id.on_sell_content_list)
    public RecyclerView mContentRv;

    @BindView(R.id.on_sell_refresh_layout)
    public TwinklingRefreshLayout mRefreshLayout;

    @BindView(R.id.fragment_layout_title)
    public TextView barTitleTv;

    private OnSellContentAdapter mAdapter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();
    }

    //这里复写掉BaseFragment的loadRootView,因为BaseFragment是只有Fragment，我们需要为精选和优惠特惠添加一个bar用来显示当前在哪个pgae中，
    // 所以就重写这个方法,新的layout有一个bar在头部
    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void initView(View view) {
        //设置布局管理器
        //两列
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),DEFALUT_SPAN_COUNT);
        mContentRv.setLayoutManager(gridLayoutManager);
        mAdapter = new OnSellContentAdapter();
        mContentRv.setAdapter(mAdapter);
        mContentRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),2.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),2.5f);
                outRect.left = SizeUtils.dip2px(getContext(),2.5f);
                outRect.right = SizeUtils.dip2px(getContext(),2.5f);
            }
        });

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableOverScroll(false);
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if(mOnSellPagePresenter != null){
                    mOnSellPagePresenter.loaderMore();
                }
            }
        });

        mAdapter.setOnSellContentItmeClickListener(this);
    }

    @Override
    protected void release() {
        super.release();
        mOnSellPagePresenter.unregisterViewCallback(this);
    }

    @Override
    protected void onRetryClick() {
        if(mOnSellPagePresenter != null){
            mOnSellPagePresenter.getOnSellContent();
        }
    }

    //=================================callBack=============================
    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        //获取的数据回来
        //更新UI
        setUpStates(State.SUCCESS);
        mAdapter.setData(result);
    }

    /**
     * 加载更多数据回来
     * @param moreResult
     */
    @Override
    public void onMoreLoaded(OnSellContent moreResult) {
        if(mRefreshLayout != null){
            mRefreshLayout.finishLoadmore();
        }
        mAdapter.setLoadMoreData(moreResult);
        int size = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtil.showToast("加载了" + size + "条记录");
    }

    @Override
    public void onMoreLoadedError() {
        if(mRefreshLayout != null){
            mRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("网络加载错误，请稍后再试。。。");
    }

    @Override
    public void onMoreLoadedEmpty() {
        if(mRefreshLayout != null){
            mRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("没有更多数据。。。");
    }

    @Override
    public void onError() {
        setUpStates(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpStates(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpStates(State.EMPTY);
    }

    //=================================callBack=============================

    //=================================Adapter callBack=====================
    @Override
    public void onItemClick(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item) {

        String title = item.getTitle();
        String url = item.getCoupon_click_url();
        if(TextUtils.isEmpty(url)){
            //因为有一些是没有优惠的，所以要跳到另一个url
            url = item.getClick_url();
        }
        String cover = item.getPict_url();
        ITicketPresenter tickPresenter = PresenterManager.getInstance().getTickPresenterImp();
        tickPresenter.getTicket(title,url,cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
    //=================================Adapter callBack=====================
}
