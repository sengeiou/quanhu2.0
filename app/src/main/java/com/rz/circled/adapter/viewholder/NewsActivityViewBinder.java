package com.rz.circled.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rz.circled.R;
import com.rz.circled.adapter.viewholder.extra.NewsActivityExtra;
import com.rz.httpapi.bean.NewsBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by rzw2 on 2017/9/7.
 */
public class NewsActivityViewBinder extends ItemViewBinder<NewsBean, NewsActivityViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_news_type_activity, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull NewsBean item) {
        holder.tvTime.setText(item.getCreateTime());
        holder.tvTitle.setText(item.getTitle());
        holder.tvDesc.setText(item.getContent());
        Glide.with(holder.itemView.getContext()).load(item.getImg()).placeholder(R.mipmap.ic_default_holder).error(R.mipmap.ic_default_holder).into(holder.img);
        NewsActivityExtra extra = new Gson().fromJson(item.getBody().toString(), NewsActivityExtra.class);
        holder.tvFrom.setText(String.format(holder.itemView.getContext().getString(R.string.private_group_from), extra.getCircleName()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.img)
        RoundedImageView img;
        @BindView(R.id.tv_from)
        TextView tvFrom;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
