package com.example.taobaounion.bases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private State currenterState = State.NONE;

    private Unbinder mBind;
    private FrameLayout mBaseContainer;
    private View mSuccessViwe;
    private View mLoadingView;
    private View mLoadErrorView;
    private View mEmptyView;

    public enum State{
        NONE,LOADING,SUCCESS,ERROR,EMPTY
    }

    @OnClick(R.id.network_error_tips)
    public void retry(){
        //点击了需要重新加载
        LogUtils.d(this,"on retry....");
        onRetryClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater,container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStatesView(inflater,container);
        //View view = loadSuccessViwe(inflater, container);
        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();
        //各个子类在返回view前有自己的各自的加载方法，所以安排loadData这个方法给他们,让他们各自实现自己的加载数据
        loadData();
        return rootView;
    }

    protected void initListener() {
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 加载各种状态的view
     * @param inflater
     * @param container
     */
    private void loadStatesView(LayoutInflater inflater, ViewGroup container){
        //成功的View
        mSuccessViwe = loadSuccessViwe(inflater, container);
        mBaseContainer.addView(mSuccessViwe);
        //loading的View
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);
        //error的view
        mLoadErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mLoadErrorView);
        //empty的View
        mEmptyView = loadEmptyView(inflater,container);
        mBaseContainer.addView(mEmptyView);
        setUpStates(State.NONE);

    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
         return inflater.inflate(R.layout.fragment_error, container, false);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    /**
     * 子类通过这个方法来切换页面
     * @param states
     */
    public void setUpStates(State states){
        currenterState = states;
        mSuccessViwe.setVisibility(currenterState == State.SUCCESS? View.VISIBLE:View.GONE);
        mLoadingView.setVisibility(currenterState == State.LOADING? View.VISIBLE:View.GONE);
        mLoadErrorView.setVisibility(currenterState == State.ERROR?View.VISIBLE:View.GONE);
        mEmptyView.setVisibility(currenterState == State.EMPTY?View.VISIBLE:View.GONE);
    }

    /**
     * 加载loading界面
     * @param inflater
     * @param container
     * @return
     */
    private View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        View loadingView = inflater.inflate(R.layout.fragment_loading, container, false);
        return loadingView;
    }

    protected void initView(View view){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mBind != null){
            mBind.unbind();
        }
        release();
    }

    protected void release(){
     //释放资源
    }

    protected void initPresenter(){
        //创建presenter
    }

    protected void loadData(){
        //加载数据
    }

    protected View loadSuccessViwe(LayoutInflater inflater, ViewGroup container){
        int resId = getRootViewResId();
        return inflater.inflate(resId,container,false);
    }

    protected void onRetryClick(){

    }

    protected abstract int getRootViewResId();
}
