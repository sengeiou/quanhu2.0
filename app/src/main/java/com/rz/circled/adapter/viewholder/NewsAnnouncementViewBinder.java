package com.rz.circled.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rz.circled.R;
import com.rz.circled.helper.NewsJumpHelper;
import com.rz.httpapi.bean.NewsBean;
import com.rz.httpapi.bean.NewsOverviewBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by rzw2 on 2017/9/7.
 */
public class NewsAnnouncementViewBinder extends ItemViewBinder<NewsBean, NewsAnnouncementViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_news_announcement, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull NewsBean item) {
        holder.item = item;
        holder.tvTitle.setText(item.getTitle());
        holder.tvTime.setText(item.getCreateTime());
        Glide.with(holder.itemView.getContext()).load(item.getImg()).placeholder(R.mipmap.ic_default_bg).error(R.mipmap.ic_default_bg).into(holder.img);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        RoundedImageView img;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;

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
