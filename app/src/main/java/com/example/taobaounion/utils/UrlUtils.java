package com.example.taobaounion.utils;

public class UrlUtils {
    public static String createHomePagerUrl(int materiaId ,int page){
        return "discovery/"+materiaId+"/"+page;
    }

    //老的https://gw.alicdn.com/bao/uploaded/i1/2599245023/O1CN01T9RhZH1mya4TWFAYF_!!0-item_pic.jpg
    //在后面添加_140x140.jpg就可以请求到140x140的图片，注意280无法申请，我也不知道为什么
    //新的https://gw.alicdn.com/bao/uploaded/i1/2599245023/O1CN01T9RhZH1mya4TWFAYF_!!0-item_pic.jpg_140x140.jpg
    public static String getCoverPath(String pict_url,int size) {
        return "https:" + pict_url + "_" + size + "x" + size + ".jpg";
    }

    public static String getCoverPath(String pict_url) {
        if(pict_url.startsWith("http:")||pict_url.startsWith("https:")){
            return pict_url;
        }else{
            return "https:" + pict_url;
        }
    }

    public static String getTicketUrl(String url) {
        if(url.startsWith("http:")||url.startsWith("https:")){
            return url;
        }else {
            return "https:" + url;
        }
    }

    public static String getSelectedPageContentUrl(int favorites_id) {
        return "recommend/" + favorites_id;
    }

    public static String getOnSellPageUrl(int currentPage) {
        return "onSell/" + currentPage;
    }
}
