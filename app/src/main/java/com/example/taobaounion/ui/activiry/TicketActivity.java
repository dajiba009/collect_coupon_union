package com.example.taobaounion.ui.activiry;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseActivity;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ITicketPagerCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    private ITicketPresenter mTickPresenterImp;

    private boolean mHashTaobaoApp = false;

    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_or_open_btn)
    public TextView mOpenOrCopyBtn;

    @BindView(R.id.ticket_back_press)
    public ImageView backPress;

    @BindView(R.id.ticket_cover_loading)
    public View loadingView;

    @BindView(R.id.ticket_load_retry)
    public View retryLoadText;


    @Override
    protected void initView() {
    }

    @Override
    protected void initEvent() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOpenOrCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //复制淘口令
                //拿到内容
                String ticketCode = mTicketCode.getText().toString().trim();
                LogUtils.d(TicketActivity.class,"ticketCode =======> " + ticketCode);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //复制到粘贴板上
                ClipData clipData = ClipData.newPlainText("taobaoUnion_ticket_code",ticketCode);
                cm.setPrimaryClip(clipData);
                //判断有没有淘宝
                //如果有就打开淘宝
                if(mHashTaobaoApp){
                    //如果有就打开淘宝
                    Intent taobaoIntent = new Intent();
                    //下面这种是比较常见用的隐式Intent，但是可能会有很多应用都用这个默认的category，所以不好用,我们使用ComponentName来拉起淘宝应用
//                    taobaoIntent.setAction("android.intent.action.MAIN");
//                    taobaoIntent.addCategory("android.intent.category.LAUNCHER");
                    //cmp=com.taobao.taobao/com.taobao.tao.TBMainActivity，我们用这个主界面，不使用welcome界面
                    ComponentName componentName = new ComponentName("com.taobao.taobao","com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                }else {
                    //如果没有就复制
                    ToastUtil.showToast("已经复制，粘贴分享，或打开淘宝");
                }
            }
        });
    }

    @Override
    protected void initPresenter() {
        mTickPresenterImp = PresenterManager.getInstance().getTickPresenterImp();
        if(mTickPresenterImp != null){
            mTickPresenterImp.registerViewCallback(this);
        }
        //判断是否有安装淘宝应用
        //act=android.intent.action.MAIN
        // cat=[android.intent.category.LAUNCHER]
        // flg=0x10200000
        // cmp=com.taobao.taobao/com.taobao.tao.welcome.Welcome 这个是组件名称，/后面的是那个界面
        // bnds=[171,606][417,897]} from uid 10041 and from pid 2744
        //判断是否有安装淘宝应用
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao",PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHashTaobaoApp = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHashTaobaoApp = false;
        }
        LogUtils.d(this,"mHashTaobaoApp ====> " + mHashTaobaoApp);
        //根据这个值去修改ui
        mOpenOrCopyBtn.setText(mHashTaobaoApp ? "打开淘宝领劵" : "复制淘口令");

    }

    @Override
    protected void release() {
        if (mTickPresenterImp != null) {
            mTickPresenterImp.unregisterViewCallback(this);
        }
    }

    //===============================================================CallBack======================================
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoadedSuccess(String cover, TicketResult result) {
        if(retryLoadText != null){
            retryLoadText.setVisibility(View.GONE);
        }
        if (mCover != null && !TextUtils.isEmpty(cover)) {
            String coverPath = UrlUtils.getCoverPath(cover);
            Glide.with(this).load(coverPath).into(mCover);
        }
        if(TextUtils.isEmpty(cover)){
            mCover.setImageResource(R.mipmap.no_image);
        }
        if (result != null && result.getData().getTbk_tpwd_create_response()!= null){
            mTicketCode.setText(result.getData().getTbk_tpwd_create_response().getData().getModel());
        }
        if(loadingView != null){
            loadingView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onError() {
        if(loadingView != null){
            loadingView.setVisibility(View.GONE);
        }
        if(retryLoadText != null){
            retryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if(retryLoadText != null){
            retryLoadText.setVisibility(View.GONE);
        }
        if(loadingView != null){
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmpty() {

    }
    //===============================================================CallBack======================================
}
