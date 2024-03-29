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
import com.rz.circled.adapter.viewholder.extra.NewsActivityExtra;
import com.rz.circled.adapter.viewholder.extra.NewsInteractiveExtra;
import com.rz.circled.helper.NewsJumpHelper;
import com.rz.common.utils.StringUtils;
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
        holder.item = item;

        holder.tvTime.setText(TextUtils.isEmpty(item.getCreateTime()) ? "" : StringUtils.stampToDate(StringUtils.toDate(item.getCreateTime()).getTime(), "yyyy年MM月dd日 HH:mm"));
        holder.tvTitle.setText(item.getTitle());
        holder.tvDesc.setText(item.getContent());
        Glide.with(holder.itemView.getContext()).load(item.getImg()).error(R.mipmap.ic_default_holder).into(holder.img);
        Gson gson = new Gson();
        String json = gson.toJson(item.getBody());
        NewsActivityExtra extra = gson.fromJson(json, NewsActivityExtra.class);
        String from;
        if (!TextUtils.isEmpty(extra.getCoterieId()) && extra.getCoterieId().length() > 0) {
            from = String.format(holder.itemView.getContext().getString(R.string.private_group_from_group), extra.getCoterieName());
        } else if (!TextUtils.isEmpty(extra.getCircleName()) && extra.getCircleName().length() > 0) {
            from = String.format(holder.itemView.getContext().getString(R.string.private_group_from_circled), extra.getCircleName());
        } else {
            from = "";
        }
        holder.tvFrom.setText(from);
        holder.tvFrom.setVisibility(TextUtils.isEmpty(from) ? View.INVISIBLE : View.VISIBLE);
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
