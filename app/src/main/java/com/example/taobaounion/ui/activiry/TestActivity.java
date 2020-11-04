package com.example.taobaounion.ui.activiry;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自定义navigationBar导航栏
 */
public class TestActivity extends Activity {

    @BindView(R.id.test_navigation_bar)
    public RadioGroup mNavigationBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initListener();
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
