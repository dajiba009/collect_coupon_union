package com.example.taobaounion.ui.custom;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.example.taobaounion.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class LoadingView extends AppCompatImageView {
    private float mDegrees = 0;
    private boolean mNeedRotate = true;


    public LoadingView(@NonNull Context context) {
        this(context,null);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRotate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRoate();
    }

    private void startRotate() {
        mNeedRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                mDegrees += 10;
                if(mDegrees > 360){
                    mDegrees = 0;
                }
                invalidate();
                //判断是否要继续旋转，
                //如果不可见，或者已经onDetachedFromWindow就不再选转
                if(getVisibility() != VISIBLE && !mNeedRotate){
                    removeCallbacks(this);
                }else {
                    postDelayed(this,25);
                }
            }
        });
    }

    private void stopRoate() {
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegrees,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }
}
