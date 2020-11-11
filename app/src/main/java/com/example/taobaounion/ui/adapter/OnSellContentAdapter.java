package com.example.taobaounion.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OnSellContentAdapter extends RecyclerView.Adapter<OnSellContentAdapter.InnerHold> {

    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnSellContentItmeClickListener mOnSellContentItemClickback = null;

    @NonNull
    @Override
    public OnSellContentAdapter.InnerHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnSellContentAdapter.InnerHold holder, int position) {
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean = mData.get(position);
        holder.setData(mapDataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSellContentItemClickback.onItemClick(mapDataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class InnerHold extends RecyclerView.ViewHolder {

        @BindView(R.id.on_sell_cover)
        public ImageView cover;

        @BindView(R.id.on_sell_content_title_tv)
        public TextView titleTv;

        @BindView(R.id.on_sell_original_price_tv)
        public TextView originalPriceTv;

        @BindView(R.id.on_sell_off_price_tv)
        public TextView offPriseTv;

        public InnerHold(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean data){
            titleTv.setText(data.getTitle());
            //LogUtils.d(this,"Picture url ====> " + data.getPict_url());
            String coverPath = UrlUtils.getCoverPath(data.getPict_url());
            Glide.with(cover.getContext()).load(coverPath).into(cover);
            String originalPrice = data.getZk_final_price();
            originalPriceTv.setText("￥" + originalPrice);
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            int couponAmount = data.getCoupon_amount();
            float originalPriceFloat = Float.parseFloat(originalPrice);
            float finalPrice = originalPriceFloat - couponAmount;
            offPriseTv.setText("劵后价格："+String.format("%.2f",finalPrice));

        }
    }

    public void setData(OnSellContent result){
        this.mData.clear();
        mData.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    public void setLoadMoreData(OnSellContent moreResult) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreData = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        mData.addAll(moreData);
        int oldPostion = mData.size();
        notifyItemRangeChanged(oldPostion -1 ,moreData.size());
    }

    public void setOnSellContentItmeClickListener(OnSellContentItmeClickListener listener){
        this.mOnSellContentItemClickback = listener;
    }

    public interface OnSellContentItmeClickListener{
        void onItemClick(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean);
    }
}
