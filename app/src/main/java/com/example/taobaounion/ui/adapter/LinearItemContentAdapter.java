package com.example.taobaounion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.ILinearItemInfo;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LinearItemContentAdapter extends RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder> {

    private List<ILinearItemInfo> data = new ArrayList<>();
    private OnListenerItemClickListener mItemClickListener = null;

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView coverIv;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.off_price)
        public TextView offPriceTv;

        @BindView(R.id.goods_after_off_price)
        public TextView finalPriceTv;

        @BindView(R.id.goods_original_price)
        public TextView originalPriceTv;

        @BindView(R.id.sell_count)
        public TextView sellCountTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(ILinearItemInfo dataBean) {
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());
            //通过获取imageView来寻找适合这个imageView大小的图片，太大会占用内存
            //ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            //这里为让图片动态适应view的大小，但因为服务器不是自己写的原因，导致容易出错，所以就不用了
//            int width = layoutParams.width;
//            int hight = layoutParams.height;
//            int coverSize = (width > hight ? width : hight)/2;
            //Glide.with(context).load(UrlUtils.getCoverPath(dataBean.getCover(),coverSize)).into(cover);
            String cover = dataBean.getCover();
            if(!TextUtils.isEmpty(cover)){
                String coverPath = UrlUtils.getCoverPath(dataBean.getCover());
                Glide.with(context).load(coverPath).into(this.coverIv);
            }else {
                coverIv.setImageResource(R.mipmap.ic_launcher);
            }
            String original = dataBean.getFinalPrice();
            String finalPrice = dataBean.getFinalPrice();
            long couponAmount = dataBean.getCouponAmout();
            float resultPrice = Float.valueOf(finalPrice) - couponAmount;
            offPriceTv.setText(String.format(context.getResources().getString(R.string.text_goods_off_price),couponAmount));
            finalPriceTv.setText(String.format("%.2f",resultPrice));
            //中划线效果
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriceTv.setText(String.format(context.getResources().getString(R.string.text_goods_original_price),original));
            sellCountTv.setText(String.format(context.getResources().getString(R.string.text_goods_sell_count),dataBean.getVolume()));
        }
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content,parent,false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //设置数据
        holder.setData(data.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null){
                    ILinearItemInfo item = data.get(position);
                    mItemClickListener.onItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }


    public void addData(List<? extends ILinearItemInfo> contents) {
        //添加前拿到之前的size
        int olderSize = data.size();
        data.addAll(contents);
        //更新UI
        notifyItemRangeChanged(olderSize,contents.size());
    }

    //原先是使用这个类的HomePagerContent.DataBean ，但为了抽出来处理我们将HomePagerContent.DataBean实现了IBaseInfo
    //下面也是同理
    public void setOnListenerItemClickListener(OnListenerItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public interface OnListenerItemClickListener{
        void onItemClick(IBaseInfo item);
    }
}
