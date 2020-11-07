package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.example.taobaounion.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class AutoLoopViewpager extends ViewPager {
    //切换间隔时长，单位毫秒
    private static final long DEFAULT_DURATION = 3000;
    private  long mDuration = DEFAULT_DURATION;

    public AutoLoopViewpager(@NonNull Context context) {
        super(context);
    }

    public AutoLoopViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopStyle);
        mDuration = a.getInteger(R.styleable.AutoLoopStyle_duration,(int)DEFAULT_DURATION);
        a.recycle();
    }

    private boolean isLoop = false;

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem ++;
            setCurrentItem(currentItem);
            if(isLoop){
                postDelayed(this,mDuration);
            }
        }
    };

    public void startLoop(){
        isLoop = true;
        //先拿到当前的位置
        post(mTask);
    }

    public void stopLoop(){
        isLoop = false;
        removeCallbacks(mTask);
    }

    /**
     * 设置切换间隔时长
     * @param duration
     */
    public void setDuration(long duration){
        this.mDuration = duration;
    }
}
