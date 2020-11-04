package com.example.taobaounion.ui.activiry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseFragment;
import com.example.taobaounion.ui.fragment.HomeFragment;
import com.example.taobaounion.ui.fragment.RedPacketFragment;
import com.example.taobaounion.ui.fragment.SearchFragment;
import com.example.taobaounion.ui.fragment.SelectedFragment;
import com.example.taobaounion.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private RedPacketFragment mRedPacketFragment;
    private SelectedFragment mSelectedFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBind = ButterKnife.bind(this);
        initFragment();
        initListener();
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mRedPacketFragment = new RedPacketFragment();
        mSelectedFragment = new SelectedFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();
        //默认选中第一页
        switchFragment(mHomeFragment);
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG,"title ---> " + item.getTitle() + " id ----> " + item.getItemId());
                if(item.getItemId() == R.id.home){
                    //这里的id为menu里面定义的Id
                    LogUtils.d(this,"切换到首页");
                    switchFragment(mHomeFragment);
                }else if(item.getItemId() == R.id.selected){
                    LogUtils.d(this,"切换到精选");
                    switchFragment(mSelectedFragment);
                }else if(item.getItemId() == R.id.red_packet){
                    LogUtils.d(this,"切换到红包");
                    switchFragment(mRedPacketFragment);
                }else if(item.getItemId() == R.id.search){
                    LogUtils.d(this,"切换到搜索");
                    switchFragment(mSearchFragment);
                }
                return true;
            }
        });
    }

    private void switchFragment(BaseFragment target) {
        FragmentTransaction transaction = mFm.beginTransaction();//开启事务
        transaction.replace(R.id.main_page_container,target);//替换
        transaction.commit();//提交事务
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBind != null){
            mBind.unbind();
        }
    }
}