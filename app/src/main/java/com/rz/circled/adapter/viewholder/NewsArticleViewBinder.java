package com.rz.circled.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rz.circled.R;
import com.rz.circled.adapter.viewholder.extra.NewsArticleExtra;
import com.rz.circled.helper.NewsJumpHelper;
import com.rz.httpapi.bean.NewsBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by rzw2 on 2017/9/7.
 */
public class NewsArticleViewBinder extends ItemViewBinder<NewsBean, NewsArticleViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_news_type_article, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull NewsBean item) {
        holder.item = item;
        holder.tvTime.setText(item.getCreateTime());
        holder.tvTitle.setText(item.getTitle());
        holder.tvDesc.setText(item.getContent());
        NewsArticleExtra extra = new Gson().fromJson(item.getBody().toString(), NewsArticleExtra.class);
        Glide.with(holder.itemView.getContext()).load(extra.getBodyImg()).placeholder(R.mipmap.ic_default_bg).error(R.mipmap.ic_default_bg).into(holder.img);
        holder.tvContent.setText(extra.getBodyTitle());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tv_content)
        TextView tvContent;

        NewsBean item;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsJumpHelper.startAcceptActivity(v.getContext(), item);
                }
            });
        }
    }
}
