package com.example.taobaounion.ui.activiry;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.taobaounion.R;
import com.example.taobaounion.ui.custom.TextFlowLayout;
import com.example.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自定义navigationBar导航栏
 */
public class TestActivity extends Activity {

    @BindView(R.id.test_navigation_bar)
    public RadioGroup mNavigationBar;

    @BindView(R.id.flow_layout)
    public TextFlowLayout mTextFlowLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initListener();
        List<String> list = new ArrayList<>();
        list.add("键盘");
        list.add("鼠标");
        list.add("张洪嘉赛加");
        list.add("终极格斗");
        list.add("android——kotlin终极血马教学");
        list.add("web终极教学");
        list.add("java 和 c++ 和c");
        list.add("显示屏");
        mTextFlowLayout.setTextList(list);
        mTextFlowLayout.setOnFlowTextItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowItemClick(String text) {
                LogUtils.d(TestActivity.this,"text ====> " + text);
            }
        });
    }

    private void initListener() {
        mNavigationBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.d(TestActivity.class,"checkedId ----- > " + checkedId);
                if(checkedId == R.id.test_home){
                    //这里的id为menu里面定义的Id
                    LogUtils.d(this,"切换到首页");
                }else if(checkedId == R.id.test_selected){
                    LogUtils.d(this,"切换到精选");
                }else if(checkedId == R.id.test_red_packet){
                    LogUtils.d(this,"切换到红包");
                }else if(checkedId == R.id.test_searcher){
                    LogUtils.d(this,"切换到搜索");
                }
            }
        });
    }
}
