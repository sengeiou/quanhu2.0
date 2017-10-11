package com.rz.circled.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rz.circled.R;
import com.rz.circled.adapter.viewholder.extra.NewsArticleExtra;
import com.rz.circled.adapter.viewholder.extra.NewsGroupExtra;
import com.rz.circled.helper.NewsJumpHelper;
import com.rz.httpapi.bean.NewsBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by rzw2 on 2017/9/7.
 */
public class NewsGroupViewBinder extends ItemViewBinder<NewsBean, NewsGroupViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_news_type_group, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull NewsBean item) {
        holder.item = item;
        holder.tvTime.setText(item.getCreateTime());
        holder.tvTitle.setText(item.getTitle());
        Glide.with(holder.itemView.getContext()).load(item.getImg()).error(R.mipmap.ic_default_private_group_icon).into(holder.avatar);
        NewsGroupExtra extra = new Gson().fromJson(item.getBody().toString(), NewsGroupExtra.class);
        String from = TextUtils.isEmpty(extra.getCoterieId()) ? extra.getCircleName() : extra.getCoterieName();
        holder.tvFrom.setText(String.format(holder.itemView.getContext().getString(R.string.private_group_from), from));
        holder.tvScan.setText(String.format(holder.itemView.getContext().getString(R.string.private_group_joined_user), extra.getMemberNum()));
        holder.tvDesc.setText(extra.getOwnerName() + "  " + extra.getOwnerIntro());
        if (extra.getJoinFee() == 0) {
            holder.tvStatus.setText(R.string.private_group_free);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.font_color_blue));
        } else {
            holder.tvStatus.setText(String.format(holder.itemView.getContext().getString(R.string.private_group_price), extra.getJoinFee()));
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.color_F5CD45));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.avatar)
        RoundedImageView avatar;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_scan)
        TextView tvScan;
        @BindView(R.id.tv_from)
        TextView tvFrom;
        @BindView(R.id.tv_status)
        TextView tvStatus;

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
