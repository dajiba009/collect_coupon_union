package com.example.taobaounion.model.domain;

/**
 * 这个接口是用来给TicketActivity的Bean实现的，让ticker显示要用到的Bean来实习，这样可以抽出来，不用过多复写代码
 */
public interface IBaseInfo {

    /**
     * 商品的封面
     * @return
     */
    String getCover();

    /**
     * 商品的标题
     * @return
     */
    String getTitle();

    /**
     * 商品的url
     * @return
     */
    String getUrl();
}
