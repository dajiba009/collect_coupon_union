package com.example.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taobaounion.R;
import com.example.taobaounion.bases.BaseFragment;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.impl.SearchPresenter;
import com.example.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.example.taobaounion.ui.custom.TextFlowLayout;
import com.example.taobaounion.utils.KeyboardUtil;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.TicketUtil;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.ISearchViewCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchViewCallback, TextFlowLayout.OnFlowTextItemClickListener {

    private SearchPresenter mSearchPresenter;

    @BindView(R.id.search_history_view)
    public TextFlowLayout mHistoriesView;

    @BindView(R.id.search_recommend_view)
    public TextFlowLayout mRecommendView;

    @BindView(R.id.search_recommend_container)
    public View mRecommendContainer;

    @BindView(R.id.search_history_container)
    public View mHistoiesContainer;

    @BindView(R.id.search_history_delete)
    public View mHistoryDelete;

    @BindView(R.id.search_recycle_view)
    public RecyclerView searchRecycleView;

    @BindView(R.id.refresh_layout)
    public TwinklingRefreshLayout mRefreshLayout;

    @BindView(R.id.search_btn)
    public TextView mSearchBtn;

    @BindView(R.id.search_clean_btn)
    public ImageView mCleanInputBtn;

    @BindView(R.id.search_input_box)
    public EditText mSearchInputBox;

    //通过使用IBaseInfo和ILinearItemInfo来是adapter和数据复用
    private LinearItemContentAdapter mAdapter;

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout,container,false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        searchRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new LinearItemContentAdapter();
        searchRecycleView.setAdapter(mAdapter);
        searchRecycleView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),1.5f);
            }
        });
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取搜索推荐词
        mSearchPresenter.getRecommendWords();
        //mSearchPresenter.doSearch("毛衣");
        mSearchPresenter.getHistories();
        setUpStates(State.SUCCESS);
    }

    @Override
    protected void initListener() {
        mRecommendView.setOnFlowTextItemClickListener(this);
        mHistoriesView.setOnFlowTextItemClickListener(this);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mSearchInputBox.getText().toString().trim())){
                    ToastUtil.showToast("请输入搜索内容");
                    return;
                }
                //如果有内容
                //如果搜索框没有内容
                if (hasInput(false)) {
                    //不包含空格
                    //发起搜索
                    if (mSearchPresenter != null) {
                        //mSearchPresenter.doSearch(mSearchInputBox.getText().toString().trim());
                        toSearch(mSearchInputBox.getText().toString().trim());
                        KeyboardUtil.hide(getContext(),v);
                    }
                }else {
                    //todo:
                }
            }
        });

        //清楚框里的内容，和返回到历史显示的界面
        mCleanInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mSearchInputBox.getText().toString().trim())){
                    mSearchInputBox.setText("");
                    //回到历史界面
                    switch2HistoryPage();
                }
            }
        });
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCleanInputBtn.setVisibility(hasInput(true) ? View.VISIBLE : View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSearchPresenter != null){
                    mSearchPresenter.delHistories();
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if(mSearchPresenter != null){
                    mSearchPresenter.loaderMore();
                }
            }
        });

        mAdapter.setOnListenerItemClickListener(new LinearItemContentAdapter.OnListenerItemClickListener() {
            @Override
            public void onItemClick(IBaseInfo item) {
                TicketUtil.toTicketPage(getContext(),item);
            }
        });

        mSearchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LogUtils.d(SearchFragment.class,v.getText().toString());
                String keyWord = v.getText().toString();
                if(actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null){
                    //判断是否为空
                    if(TextUtils.isEmpty(keyWord)){
                        return false;
                    }else {
                        //发起搜索
                        //mSearchPresenter.doSearch(keyWord);
                        toSearch(keyWord);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onRetryClick() {
        if(mSearchPresenter != null){
            mSearchPresenter.research();
        }
    }

    @Override
    protected void release() {
        mSearchPresenter.unregisterViewCallback(this);
    }

    //==================================抽取部分================

    //判断是否包含空格
    private boolean hasInput(boolean containSpace){
        if(containSpace){
            return mSearchInputBox.toString().length() > 0;
        }else {
            return mSearchInputBox.toString().trim().length() > 0;
        }
    }


    /**
     * 切换到历史和推荐页面
     */
    private void switch2HistoryPage() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
        if(mRecommendView.getContentSize() != 0){
            mRecommendContainer.setVisibility(View.VISIBLE);
        }else {
            mRecommendContainer.setVisibility(View.GONE);
        }
        searchRecycleView.setVisibility(View.GONE);
    }
    //==================================抽取部分================

    //===========================================callBack========================
    @Override
    public void onHistoriesLoaded(Histories histories) {
        LogUtils.d(this,"histories ---> " + histories);
        if(histories == null || histories.getHistories().size() == 0){
            mHistoiesContainer.setVisibility(View.GONE);
        }else {
            mHistoiesContainer.setVisibility(View.VISIBLE);
            mHistoriesView.setTextList(histories.getHistories());
        }
    }

    @Override
    public void onHitoriesDeleted() {
        mSearchPresenter.getHistories();
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        //LogUtils.d(this,"result ===> " + result);
        setUpStates(State.SUCCESS);
        //隐藏搜索历史和热门搜索
        mRecommendContainer.setVisibility(View.GONE);
        mHistoiesContainer.setVisibility(View.GONE);
        //显示搜索界面
        searchRecycleView.setVisibility(View.VISIBLE);
        //设置数据
        try {
            //有可能返回来的结果getResult_list返回为null，所以用setUpStates(State.EMPTY)
            mAdapter.setData(result.getData().
                    getTbk_dg_material_optional_response().
                    getResult_list().
                    getMap_data());
        }catch (Exception e){
            e.printStackTrace();
            setUpStates(State.EMPTY);
        }
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        //加载更多的结果
        mAdapter.addData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        ToastUtil.showToast("加载了" + result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() + "条结果");
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void onMoreLoaderError() {
        ToastUtil.showToast("加载出错了。。。。");
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void onMoreLoadedEmpty() {
        ToastUtil.showToast("数据为空。。。。");
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        LogUtils.d(this,"recommendWords size =====> " + recommendWords.size());
        List<String> list = new ArrayList<>();
        for (SearchRecommend.DataBean recommendWord : recommendWords) {
            list.add(recommendWord.getKeyword());
        }
        if(recommendWords == null || recommendWords.size() == 0){
            mRecommendContainer.setVisibility(View.GONE);
        }else {
            mRecommendContainer.setVisibility(View.VISIBLE);
            mRecommendView.setTextList(list);
        }
    }

    @Override
    public void onError() {
        setUpStates(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpStates(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpStates(State.EMPTY);
    }

    //===========================================callBack=========================

    //========================================自定义view的监听==========
    @Override
    public void onFlowItemClick(String text) {
        toSearch(text);
    }

    //========================================自定义view的监听==========

    /**
     * 将doSearch抽出来，然后可以在search前做一些初始化的操作
     * @param text
     */
    private void toSearch(String text) {
        if(mSearchPresenter != null){
            searchRecycleView.scrollToPosition(0);
            mSearchInputBox.setText(text);
//            mSearchInputBox.requestFocus()t
            mSearchInputBox.setSelection(text.length(),text.length());
            mSearchPresenter.doSearch(text);
        }
    }
}
