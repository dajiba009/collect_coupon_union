package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class TbNestedScrollView extends NestedScrollView {
    private int mHeaderHeight = 0;
    private int originScroll = 0;
    private RecyclerView mRecyclerView;
    private String TAG = "TbNestedScrollView";

    public TbNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if(target instanceof RecyclerView){
            this.mRecyclerView = (RecyclerView)target;
        }
//        Log.d(TAG,"mHeaderHeight ===> " + mHeaderHeight);
//        Log.d(TAG,"originScroll ===> " + originScroll);
        if(mHeaderHeight > originScroll){
            //scrollBy是滑动多少，scrollTo是滑动到某个为位置
            scrollBy(dx,dy);
            //将dx和dy消费掉
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //改变后y的位置
        this.originScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setHeaderHeight(int headerHeight){
        this.mHeaderHeight = headerHeight;
    }

    /**
     * 判断子类是否已经滑到底部
     * @return
     */
    public boolean isInBottom() {
        if (mRecyclerView != null) {
            //这个方法是来判断是否能继续垂直方向滑到，不能滑到返回false，能滑动返回true
            //所以我们可以通过这个来判断是否滑到底部
            //整数表示检测是否能往下滑，若返回true则是不能往下滑了，负数表示检测是否能往上滑，
            boolean isBottom = mRecyclerView.canScrollVertically(1);
            Log.d(TAG,"isBottom ===> " + isBottom);
            return !isBottom;
        }
        return false;
    }
}
