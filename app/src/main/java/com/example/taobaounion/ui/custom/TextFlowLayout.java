package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TextFlowLayout extends ViewGroup {

    private final static int DEFAULT_SPACE = 10;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private float mItemHorizonalSpace = DEFAULT_SPACE;

    private List<String> mTextList = new ArrayList<>();
    private int mSelfWidth;
    private int mItemHeight;
    private OnFlowTextItemClickListener mItemClickerListener = null;

    public TextFlowLayout(Context context) {
        this(context,null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);
        mItemHorizonalSpace = ta.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FlowTextStyle_verticalSpace,DEFAULT_SPACE);
        ta.recycle();

    }

    public void setTextList(List<String> textList){
        for (String text : textList) {
            this.mTextList.add(text);
            TextView item = (TextView) LayoutInflater.from(this.getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemClickerListener != null){
                        mItemClickerListener.onFlowItemClick(text);
                    }
                }
            });
            this.addView(item);
        }
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    public float getItemHorizonalSpace() {
        return mItemHorizonalSpace;
    }

    public void setItemHorizonalSpace(float itemHorizonalSpace) {
        mItemHorizonalSpace = itemHorizonalSpace;
    }


    //用来存放行的List
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getChildAt(0) == null){
            return;
        }
        //因为onMeasure会被父类容器多次调用测量，所以每次测量都要重新清空
        //用来存放一行itemView的list，每次调用都会清空这个line
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec - getPaddingLeft() - getPaddingRight());
        //ViewGroup自身的宽度
        //LogUtils.d(this,"parentView mSelfWidth =====> " + mSelfWidth);
        //测量子view
        int childCount = getChildCount();
        //测量前子view的宽度，因为还没measureChild，所以子view的width 应该为0
        //LogUtils.d(this,"childView width ===> " + getChildAt(0).getMeasuredWidth());
        //下面是测量所有子view的宽度，然后判断添加多少行，
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if(itemView.getVisibility() != VISIBLE){
                continue;
            }
            //测量子的view，通过父类的宽和高帮助子view的测量，这样itemView就会有宽和高的值
            measureChild(itemView,widthMeasureSpec,heightMeasureSpec);
            //测量后的子view
            if(line == null){
                line = createNewLine(itemView);
            }else {
                if(canBeAdd(line,itemView)){
                    line.add(itemView);
                }else {
                    line = createNewLine(itemView);
                }
            }
        }
        mItemHeight = getChildAt(0).getMeasuredHeight();
        //下面是测量view的高度
        int selfHeight = (int) (mItemHeight * lines.size() + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        //测量自己
        setMeasuredDimension(mSelfWidth,selfHeight);

    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    private boolean canBeAdd(List<View> line,View itemView) {
        int toatalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            toatalWidth += view.getMeasuredWidth();
        }
        toatalWidth += (line.size()+1) * mItemHorizonalSpace;
        LogUtils.d(this,"toatalWidth width ====> " + toatalWidth);
        LogUtils.d(this,"mSelfWidth width ====> " + mSelfWidth);
        return mSelfWidth >= toatalWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topOffset = (int) mItemVerticalSpace;
        for (List<View> views : lines) {
            //views是每一行
            int leftOffset = (int) mItemHorizonalSpace;
            for (View view : views) {
                //我感觉应该不为getMeasuredWidth,应该为view.getWidth(),getwidth为最终测量结果，getMeasuredWidth会被多次测量导致不准确
                view.layout(leftOffset,topOffset,leftOffset + view.getMeasuredWidth(),topOffset + view.getMeasuredHeight());
                leftOffset += view.getMeasuredWidth() + mItemHorizonalSpace;
            }
            topOffset += mItemHeight + mItemVerticalSpace;
        }
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener clickListener){
        this.mItemClickerListener = clickListener;
    }

    public int getContentSize() {
        return mTextList.size();
    }

    public interface OnFlowTextItemClickListener{
        void onFlowItemClick(String text);
    }
}
