package com.example.taobaounion.utils;

public class UrlUtils {
    public static String createHomePagerUrl(int materiaId ,int page){
        return "discovery/"+materiaId+"/"+page;
    }
}
