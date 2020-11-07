package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.view.ITicketPagerCallback;

public class TickPresenterImp implements ITicketPresenter {
    @Override
    public void getTicket(String title, String url, String cover) {
        //todo：获取淘口令
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {

    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {

    }
}
