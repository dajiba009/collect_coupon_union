package com.example.taobaounion.utils;

import android.util.Log;

public class LogUtils {

    private static final int CURRENTLEV = 4;
    private static final int DEBUGLEV = 4;
    private static final int INFOLEV = 3;
    private static final int WARNINGLV = 2;
    private static final int ERRORLV = 1;

    public static void d(Object object,String log){
        if(CURRENTLEV >= DEBUGLEV){
            Log.d(object.getClass().getSimpleName(),log);
        }
    }

    public static void i(Object object,String log){
        if(CURRENTLEV >= INFOLEV){
            Log.d(object.getClass().getSimpleName(),log);
        }
    }

    public static void w(Object object,String log){
        if(CURRENTLEV >= WARNINGLV){
            Log.d(object.getClass().getSimpleName(),log);
        }
    }

    public static void e(Object object,String log){
        if(CURRENTLEV >= ERRORLV){
            Log.d(object.getClass().getSimpleName(),log);
        }
    }

}
