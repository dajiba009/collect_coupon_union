package com.example.taobaounion.ui.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPageContentAdapter extends RecyclerView.Adapter<SelectPageContentAdapter.InnerHolder> {

    private List<SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnSelectedPageContentItemClickListener mContentItemClickListener = null;

    @NonNull
    @Override
    public SelectPageContentAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_content,parent,false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPageContentAdapter.InnerHolder holder, int position) {
        SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData = mData.get(position);
        holder.setData(itemData);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(itemData.getCoupon_click_url())){
                    ToastUtil.showToast("没有优惠劵拉。。。");
                }else{
                    if(mContentItemClickListener != null){
                        mContentItemClickListener.onContentItemClick(itemData);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedContent content) {
        if(content.getCode() == Constants.SUCCESS_CODE){
            List<SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> map_data =
                    content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            this.mData.clear();;
            this.mData.addAll(map_data);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.selected_cover)
        public ImageView cover;

        @BindView(R.id.selected_off_price)
        public TextView offPriceTv;

        @BindView(R.id.selected_title)
        public TextView title;

        @BindView(R.id.select_buy_btr)
        public TextView buyBtn;

        @BindView(R.id.selected_original_prise)
        public TextView originalPriseTv;

        @OnClick(R.id.select_buy_btr)
        public void getBuyCard(){
            LogUtils.d(SelectPageContentAdapter.class,"我拿到劵了哈哈哈。。。");
        }


        View innerView;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            this.innerView = itemView;
            ButterKnife.bind(this,itemView);
        }

        public void setData(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData) {
            title.setText(itemData.getTitle());
            String pictureUrl = itemData.getPict_url();
            LogUtils.d(this,"pictureUrl ====> " + pictureUrl);
            String coverPath = UrlUtils.getCoverPath(pictureUrl);
            Glide.with(itemView.getContext()).load(coverPath).into(cover);
            if(TextUtils.isEmpty(itemData.getCoupon_click_url())){
                originalPriseTv.setText("晚啦没有优惠券了。。");
                //buyBtn.setVisibility(View.GONE);
                buyBtn.setBackgroundColor(innerView.getContext().getResources().getColor(R.color.gray,null));
                buyBtn.setEnabled(false);
            }else {
                buyBtn.setBackground(innerView.getContext().getResources().getDrawable(R.drawable.shap_tag_bg,null));
                buyBtn.setVisibility(View.VISIBLE);
                originalPriseTv.setText("原价：" + itemData.getZk_final_price());
            }
            if(TextUtils.isEmpty(itemData.getCoupon_info())){
                offPriceTv.setVisibility(View.GONE);
            }else {
                offPriceTv.setVisibility(View.VISIBLE);
                offPriceTv.setText(itemData.getCoupon_info());
            }
        }
    }

    public void setOnSelectedPageContentItemClickListener(OnSelectedPageContentItemClickListener listener){
        this.mContentItemClickListener = listener;
    }

    public interface OnSelectedPageContentItemClickListener{
        void onContentItemClick(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item);
    }
}
