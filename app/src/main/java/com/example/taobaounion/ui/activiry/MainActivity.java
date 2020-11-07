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
import com.example.taobaounion.bases.BaseActivity;
import com.example.taobaounion.bases.BaseFragment;
import com.example.taobaounion.ui.fragment.HomeFragment;
import com.example.taobaounion.ui.fragment.RedPacketFragment;
import com.example.taobaounion.ui.fragment.SearchFragment;
import com.example.taobaounion.ui.fragment.SelectedFragment;
import com.example.taobaounion.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private RedPacketFragment mRedPacketFragment;
    private SelectedFragment mSelectedFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected void initEvent() {
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

    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction transaction = mFm.beginTransaction();//开启事务
        //通过修改add和hide来控制fragment的显示
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_page_container,targetFragment);
        }else {
            transaction.show(targetFragment);
        }
        if(lastOneFragment != null){
            transaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        //transaction.replace(R.id.main_page_container,targetFragment);//替换
        transaction.commit();//提交事务
    }

}