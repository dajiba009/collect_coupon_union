package com.example.taobaounion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.ui.activiry.TicketActivity;

public class TicketUtil {

    public static void toTicketPage(Context context, IBaseInfo baseInfo){
        String title = baseInfo.getTitle();
        String url = baseInfo.getUrl();
        if(TextUtils.isEmpty(url)){
            //因为有一些是没有优惠的，所以要跳到另一个url
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        ITicketPresenter tickPresenter = PresenterManager.getInstance().getTickPresenterImp();
        tickPresenter.getTicket(title,url,cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
