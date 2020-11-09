package com.example.taobaounion.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.SelectedPageCategory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {

    private OnLeftItemClickListener mOnLeftItemClickListener = null;
    private List<SelectedPageCategory.DataBean> data = new ArrayList<>();
    private int mCurrentSelectedPosition = 0;

    @NonNull
    @Override
    public SelectedPageLeftAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left,parent,false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPageLeftAdapter.InnerHolder holder, int position) {
        TextView itemTv = holder.itemView.findViewById(R.id.left_catefory_tv);
        if(mCurrentSelectedPosition == position){
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.colorEFEEEE,null));
        }else {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.white,null));
        }
        SelectedPageCategory.DataBean dataBean = data.get(position);
        itemTv.setText(dataBean.getFavorites_title());
        itemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCurrentSelectedPosition != position && mOnLeftItemClickListener != null){
                    mCurrentSelectedPosition = position;
                    mOnLeftItemClickListener.onLeftItemClick(dataBean);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setData(SelectedPageCategory result) {
        List<SelectedPageCategory.DataBean> resultData = result.getData();
        if(resultData != null){
            data.clear();
            data.addAll(result.getData());
            notifyDataSetChanged();
        }
        if(data.size() > 0){
            mOnLeftItemClickListener.onLeftItemClick(data.get(mCurrentSelectedPosition));
        }
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener){
        this.mOnLeftItemClickListener = listener;
    }

    public interface OnLeftItemClickListener{
        void onLeftItemClick(SelectedPageCategory.DataBean item);
    }
}
