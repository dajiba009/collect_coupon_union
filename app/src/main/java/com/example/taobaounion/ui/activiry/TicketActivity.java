package com.example.taobaounion.ui.activiry;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseActivity;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ITicketPagerCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    private ITicketPresenter mTickPresenterImp;

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
    }

    @Override
    protected void initPresenter() {
        mTickPresenterImp = PresenterManager.getInstance().getTickPresenterImp();
        mTickPresenterImp.registerViewCallback(this);

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
