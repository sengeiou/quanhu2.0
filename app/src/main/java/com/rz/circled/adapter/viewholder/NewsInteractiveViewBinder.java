package com.rz.circled.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rz.circled.R;
import com.rz.circled.adapter.viewholder.extra.NewsArticleExtra;
import com.rz.circled.adapter.viewholder.extra.NewsInteractiveExtra;
import com.rz.circled.helper.NewsJumpHelper;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.NewsBean;

import java.io.StringReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by rzw2 on 2017/9/7.
 */
public class NewsInteractiveViewBinder extends ItemViewBinder<NewsBean, NewsInteractiveViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_news_type_interactive, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull NewsBean item) {
        holder.item = item;
        holder.tvTime.setText(TextUtils.isEmpty(item.getCreateTime()) ? "" : StringUtils.formatDisplayTime(item.getCreateTime()));
        holder.tvTitle.setText(item.getContent());
        Gson gson = new Gson();
        String json = gson.toJson(item.getBody());
        NewsInteractiveExtra extra = gson.fromJson(json, NewsInteractiveExtra.class);
        holder.tvName.setText(extra.getCustName());
        if (TextUtils.isEmpty(item.getImg())) {
            holder.img.setVisibility(View.GONE);
        } else {
            Glide.with(holder.itemView.getContext()).load(extra.getBodyImg()).error(R.mipmap.ic_default_bg).into(holder.img);
            holder.img.setVisibility(View.VISIBLE);
        }
        Glide.with(holder.itemView.getContext()).load(extra.getCustImg()).error(R.mipmap.ic_default_avatar_small).into(holder.avatar);
        holder.tvContent.setText(extra.getBodyTitle());
        String from = TextUtils.isEmpty(extra.getCoterieId()) ? (TextUtils.isEmpty(extra.getCircleName()) ? "" : extra.getCircleName()) : extra.getCoterieName();
        holder.tvFrom.setText(String.format(holder.itemView.getContext().getString(R.string.private_group_from), from));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.avatar)
        RoundedImageView avatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_from)
        TextView tvFrom;

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
