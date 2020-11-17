package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.TicketParams;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ITicketPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TickPresenterImp implements ITicketPresenter {

    //因为只有一个要用到这个Presenter所有不使用集合储存
    private ITicketPagerCallback mViewCallback;
    private String mCover = null;
    private TicketResult mTicketResult;

    enum LoadState{
        LOADING,SUCCESS,ERROR,NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        onTicketLoading();
        this.mCover = cover;
        //todo：获取淘口令
        LogUtils.d(this,"title =====> " + title);
        LogUtils.d(this,"url =====> " + url);
        LogUtils.d(this,"cover =====> " + cover);
        //获取过来的的url是没有http:这个前缀的，那我们要手动添加
        String targetUrl = UrlUtils.getTicketUrl(url);
        Retrofit retrofit = RetofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(targetUrl,title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                if(code == HttpURLConnection.HTTP_OK){
                    //请求成功
                    mTicketResult = response.body();
                    //LogUtils.d(TicketParams.class,"result ===》 " + ticketResult.toString());
                    //通知ui
                    onTicketLoadSuccess();
                }else {
                    //失败
                    onLoadedTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                onLoadedTicketError();
            }
        });
    }

    private void onLoadedTicketError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }else {
            mCurrentState = LoadState.ERROR;
        }
    }

    private void onTicketLoadSuccess() {
        if(mViewCallback != null){
            mViewCallback.onTicketLoadedSuccess(mCover,mTicketResult);
        }else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        this.mViewCallback = callback;
        //之所以有下面这个的原因是获取数据是在new TicketActivity之前，所以可能会出现一种情况是，获取数据过快，比TicketActivity加载完页面前还要快，回导致
        //加载将数获取到的数据导入不了TicketActivity，以为TicketActivity初始化还没有完成，这样会导致导入数据失败，此时mViewCallback=null,解决方法就是，先暂时保存数据
        //此时的mCurrentState是记录下来的，我们可以通过mCurrentState来判断时机
        if(mCurrentState != LoadState.NONE){
            //说明状态已经改变了
            //更新UI
            if(mCurrentState == LoadState.SUCCESS){
                onTicketLoadSuccess();
            }else if(mCurrentState == LoadState.ERROR){
                onLoadedTicketError();
            }else if (mCurrentState == LoadState.LOADING){
                onTicketLoading();
            }
        }
    }

    private void onTicketLoading() {
        if(mViewCallback != null){
            mViewCallback.onLoading();
        }else {
            mCurrentState = LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {
        this.mViewCallback = null;
    }
}
