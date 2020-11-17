package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.ui.activiry.IMainActivity;
import com.example.taobaounion.ui.activiry.ScanQrCodeActivity;
import com.example.taobaounion.ui.adapter.HomePagerAdapter;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;
import com.vondear.rxfeature.activity.ActivityScanerCode;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    private IHomePresenter mHomePresenter;

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.home_pager)
    public ViewPager homePager;

    @BindView(R.id.home_search_inputbox)
    public View homeSearchInputBox;

    @BindView(R.id.scan_icon)
    public View scanBtn;

    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.framgent_home;
    }

    @Override
    protected void initView(View view) {
        mTabLayout.setupWithViewPager(homePager);
        //给ViewPage设置适配器
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        homePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void loadData() {
        //加载数据
        if(mHomePresenter != null){
            mHomePresenter.getCategories();
        }
    }

    @Override
    protected void initListener() {
        //点击homefragmen上的search框会跳转到SearchFragment
        homeSearchInputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if(activity instanceof IMainActivity){
                    ((IMainActivity)activity).switch2Search();
                }
            }
        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanQrCodeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initPresenter() {
        //创建presenter
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void release() {
        //释放资源
        //取消注册
        if(mHomePresenter != null){
            mHomePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        if(mHomePresenter!=null){
            mHomePresenter.getCategories();
        }
    }

    /**
     * 设置子类属于自己的xml
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout, container, false);
    }
    //=============================presenter层实现的方法=====================
    @Override
    public void onCreategories(Categories categories) {
        setUpStates(State.SUCCESS);
        //加载的数据会从这里回来
        if(mHomePagerAdapter != null){
            mHomePagerAdapter.setCategories(categories);
        }
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
    //=============================presenter层实现的方法=====================
}
