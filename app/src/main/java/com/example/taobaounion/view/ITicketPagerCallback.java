package com.example.taobaounion.view;

import com.example.taobaounion.bases.IBaseCallBack;
import com.example.taobaounion.model.domain.TicketResult;

public interface ITicketPagerCallback extends IBaseCallBack {
    /**
     * 淘口令加载结果
     * @param cover
     * @param result
     */
    void onTicketLoadedSuccess(String cover, TicketResult result);
}
