package com.example.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.example.taobaounion.ui.adapter.LooperPagerAdapter;
import com.example.taobaounion.ui.custom.AutoLoopViewpager;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.TicketUtil;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, LinearItemContentAdapter.OnListenerItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {

    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    private LinearItemContentAdapter mContentAdapter;

    @BindView(R.id.looperPage)
    public AutoLoopViewpager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView mCurrentPageTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout mRefreshLayoutreshLayout;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestView;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;

    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean dataBean){
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE,dataBean.getTitle());
        bundle.putInt(Constants.KEY_HOME_MATERIAL_ID,dataBean.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        looperPager.stopLoop();
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void initView(View view) {
        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //创建适配器
        mContentAdapter = new LinearItemContentAdapter();
        //设置适配器
        mContentList.setAdapter(mContentAdapter);
        //创建轮播图适配
        mLooperPagerAdapter = new LooperPagerAdapter();
        looperPager.setAdapter(mLooperPagerAdapter);
        //设置刷新框架
        mRefreshLayoutreshLayout.setEnableRefresh(false);
        mRefreshLayoutreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {
        mContentAdapter.setOnListenerItemClickListener(this);
        mLooperPagerAdapter.setOnLooperPageItemClickListener(this);

        //注意这里的tkrefreshlayout有一个问题就是会使RecyclerView把所有item一次性全部创建导致内存占用大,下面就是修复这个问题
        //这里是首先是通过给homePagerParent即是最里层的LinearLayout添加一个addview的监听，以为RecyclerView会创建View的，添加里面的view也会触发这个监听
        //然后我们通过动态限制RecycleView的高度，让其不能一次性加载全部itemView，只加载RecyclerView高度/itemView高度的子view，这样就可以避免因一次加载全部itemView导致占用内存过大
        //加载完后
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取头部LinearLayout的高度,将这个高度给NestedScrollView，作为什么时候停止滑动消费的相应
                if(homeHeaderContainer!=null){
                    int headerHeight = homeHeaderContainer.getMeasuredHeight();
                    homePagerNestView.setHeaderHeight(headerHeight);
                    //获取homePagerParent这个LinearLayout的高度
                    int measuredHeight = homePagerParent.getMeasuredHeight();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                    layoutParams.height = measuredHeight;
                    //将RecyclerView的高度设置为顶层LinearLayout的高度
                    mContentList.setLayoutParams(layoutParams);
                    if(measuredHeight != 0){
                        homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });

        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(mLooperPagerAdapter.getDataSize() != 0){
                    int targetPosition = position % mLooperPagerAdapter.getDataSize();
                    //切换指示器
                    updateLooperIndicator(targetPosition);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRefreshLayoutreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(HomePagerFragment.class,"加载更多内容");
                Toast.makeText(getContext(),"加载更多内容...",Toast.LENGTH_SHORT).show();

                //加载更多内容
                mCategoryPagerPresenter.loaderMore(mMaterialId);
            }
        });
    }

    /**
     * 切换指示器
     * @param targetPosition
     */
    private void updateLooperIndicator(int targetPosition) {
        //LogUtils.d(this,"targetPositon ===> " + targetPosition);
        for(int i=0;i<looperPointContainer.getChildCount();i++){
            View point = looperPointContainer.getChildAt(i);
            if(i == targetPosition){
                point.setBackgroundResource(R.drawable.shape_indicator_point_select);
            }else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String tilte = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_MATERIAL_ID);
        LogUtils.d(this,"title ===> " + tilte);
        LogUtils.d(this,"materialId ====> " + mMaterialId);
        //填充数据
        if(mCategoryPagerPresenter != null){
            mCategoryPagerPresenter.getContentByCategoryId(mMaterialId);
        }
        if(mCurrentPageTitleTv != null){
            mCurrentPageTitleTv.setText(tilte);
        }
    }

    @Override
    protected void release() {
        if(mCategoryPagerPresenter != null){
            mCategoryPagerPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        if(mCategoryPagerPresenter != null){
            mCategoryPagerPresenter.reload(mMaterialId);
        }
    }

    //===============================================================CallBack======================================
    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //加载数据
        mContentAdapter.setData(contents);
        setUpStates(State.SUCCESS);
    }

    @Override
    public void onLoading() {
        setUpStates(State.LOADING);
    }

    @Override
    public void onError() {
        setUpStates(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpStates(State.EMPTY);
    }

    @Override
    public void onLoadMoreError() {
        ToastUtil.showToast("网络异常请稍后重试");
        if(mRefreshLayoutreshLayout != null){
            mRefreshLayoutreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        ToastUtil.showToast("数据加载为空");
        if(mRefreshLayoutreshLayout != null){
            mRefreshLayoutreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents) {
        mContentAdapter.addData(contents);
        if(mRefreshLayoutreshLayout != null){
            mRefreshLayoutreshLayout.finishLoadmore();
            ToastUtil.showToast("加载了" + contents.size()+"条记录");
        }
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        mLooperPagerAdapter.setData(contents);
        looperPointContainer.removeAllViews();
//        GradientDrawable selecterDrawable = (GradientDrawable) getContext().getDrawable(R.drawable.shape_indicator_point_select);
//        GradientDrawable normalDrawable = (GradientDrawable) getContext().getDrawable(R.drawable.shape_indicator_point_select);
        //将开始设置为中间位置
        int dx = (Integer.MAX_VALUE/2) % contents.size();
        int targetCenterPosition = (Integer.MAX_VALUE/2) -dx;
        looperPager.setCurrentItem(targetCenterPosition);
        for(int i=0;i<contents.size();i++){
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(),8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size,size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(),5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(),5);
            point.setLayoutParams(layoutParams);
            if(i == 0){
                point.setBackgroundResource(R.drawable.shape_indicator_point_select);
            }else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    //===============================================================CallBack======================================

    //===============================================================adapter的点击接口======================================

    /**
     * 列表框
     * @param item
     */
    @Override
    public void onItemClick(IBaseInfo item) {
        //列表点击内容
        handleItemClick(item);
    }

    //原先是使用这个类的HomePagerContent.DataBean ，但为了抽出来处理我们将HomePagerContent.DataBean实现了IBaseInfo，后面只要去TicketAcitivy的Bean类都实现这个方法
    //很多Fragment都要走TicketActivity所以将所有的Bean类继承IBaseInfo，统一管理
    private void handleItemClick(IBaseInfo item) {
//        //因为都是跳转到tickActivity所以都是用这个方法
//        //拿到ticket去加载数据
//        String title = item.getTitle();
//        //详情地址
//        //String url = item.getClick_url();
//        String url = item.getCoupon_click_url();
//        if(TextUtils.isEmpty(url)){
//            //因为有一些是没有优惠的，所以要跳到另一个url
//            url = item.getClick_url();
//        }
//        String cover = item.getPict_url();
//        ITicketPresenter tickPresenter = PresenterManager.getInstance().getTickPresenterImp();
//        tickPresenter.getTicket(title,url,cover);
//        startActivity(new Intent(getContext(), TicketActivity.class));
        //抽出来，使用工具类来跳转到TicketActivity
        TicketUtil.toTicketPage(getContext(),item);
    }

    /**
     * 轮播图
     * @param item
     */
    @Override
    public void onLooperItemClick(IBaseInfo item) {
        handleItemClick(item);
    }
    //===============================================================adapter的点击接口======================================
}
