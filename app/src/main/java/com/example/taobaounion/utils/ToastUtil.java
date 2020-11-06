package com.example.taobaounion.utils;

import android.widget.Toast;

import com.example.taobaounion.bases.BaseApplication;

public class ToastUtil {

    private static Toast sToast;

    public static void showToast(String tips){
        if(sToast == null){
            sToast = Toast.makeText(BaseApplication.getAppContext(),tips,Toast.LENGTH_SHORT);
        }else {
            sToast.setText(tips);
        }
        sToast.show();
    }
}
