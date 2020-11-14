package com.example.taobaounion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.taobaounion.bases.BaseApplication;
import com.example.taobaounion.model.domain.CacheWithDuration;
import com.example.taobaounion.model.domain.Histories;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonCacheUtil {
    public static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private static JsonCacheUtil sJsonCacheUtil;
    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;
    private final static String HISTORIES_CACHE_KEY = "histories_cache_key";

    private JsonCacheUtil(){
        mSharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveCache(String key,Object value){
        this.saveCache(key,value,-1L);
    }

    public void saveCache(String key,Object value,long duration){
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        //=============中间层=====
//        String historiesCache = mSharedPreferences.getString(HISTORIES_CACHE_KEY, null);
//        if(historiesCache == null){
//            //todo:
//        }
        //==============end======
        //保存一个数据有时间的内容
        String valueStr = mGson.toJson(value);
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration,valueStr);
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        edit.putString(key,cacheWithTime);
        edit.apply();
    }

    public void deleteCache(String key){
        mSharedPreferences.edit().remove(key).apply();
    }

    public  <T extends Histories> T getValue(String key, Class<T> clazz){
        String valueWithDuration = mSharedPreferences.getString(key,null);
        //加入中间层来存储 ============start============
//        String historiesCache = mSharedPreferences.getString(HISTORIES_CACHE_KEY,null);
//        if(historiesCache == null){
//            return null;
//        }
//        Histories histories = mGson.fromJson(historiesCache, Histories.class);
        //===========================end===============
        if(valueWithDuration == null){
            return null;
        }
        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);
        //对时间进行判断
        long duration = cacheWithDuration.getDuration();
        //给予一个时间限制，当前时间加上duration然后取得时候当前时间相减就可以知道是否过期，
        if(duration != -1 && duration - System.currentTimeMillis() <= 0){
            //过期了
            return null;
        }else {
            //没过期
            String cache = cacheWithDuration.getCache();
            //=====中间层=====
//            histories.getHistories().add(cache);
//            return (T) histories;
            //======end======
            T result = mGson.fromJson(cache, clazz);
            return result;
        }


    }

    public static JsonCacheUtil getInstance(){
        if(sJsonCacheUtil == null){
            synchronized (JsonCacheUtil.class){
                sJsonCacheUtil = new JsonCacheUtil();
            }
        }
        return sJsonCacheUtil;
    }
}
