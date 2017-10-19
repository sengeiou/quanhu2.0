package com.rz.circled.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rz.circled.R;
import com.rz.circled.adapter.viewholder.extra.NewsActivityExtra;
import com.rz.circled.adapter.viewholder.extra.NewsArticleExtra;
import com.rz.circled.helper.NewsJumpHelper;
import com.rz.common.utils.StringUtils;
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
        holder.tvTime.setText(TextUtils.isEmpty(item.getCreateTime()) ? "" : StringUtils.stampToDate(StringUtils.toDate(item.getCreateTime()).getTime(), "yyyy年MM月dd日 HH:mm"));
        holder.tvTitle.setText(item.getTitle());
        holder.tvDesc.setText(item.getContent());
        Gson gson = new Gson();
        String json = gson.toJson(item.getBody());
        NewsArticleExtra extra = gson.fromJson(json, NewsArticleExtra.class);
        if (TextUtils.isEmpty(item.getImg())) {
            holder.img.setVisibility(View.GONE);
        } else {
            Glide.with(holder.itemView.getContext()).load(extra.getBodyImg()).error(R.mipmap.ic_default_bg).into(holder.img);
            holder.img.setVisibility(View.VISIBLE);
        }
        holder.tvContent.setText(extra.getBodyTitle());
        String from = TextUtils.isEmpty(extra.getCoterieId()) ? (TextUtils.isEmpty(extra.getCircleName()) ? "" : extra.getCircleName()) : extra.getCoterieName();
        if (TextUtils.isEmpty(from)) {
            holder.lineFrom.setVisibility(View.GONE);
        } else {
            holder.tvFrom.setText(String.format(holder.itemView.getContext().getString(R.string.private_group_from), from));
            holder.lineFrom.setVisibility(View.VISIBLE);
        }

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
        @BindView(R.id.tv_from)
        TextView tvFrom;
        @BindView(R.id.line_from)
        LinearLayout lineFrom;

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
