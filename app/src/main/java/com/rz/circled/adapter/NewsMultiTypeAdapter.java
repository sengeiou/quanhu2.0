package com.rz.circled.adapter;

import android.support.annotation.NonNull;

import com.rz.circled.adapter.viewholder.NewsActivityViewBinder;
import com.rz.circled.adapter.viewholder.NewsAnnouncementViewBinder;
import com.rz.circled.adapter.viewholder.NewsArticleViewBinder;
import com.rz.circled.adapter.viewholder.NewsGroupViewBinder;
import com.rz.circled.adapter.viewholder.NewsInteractiveViewBinder;
import com.rz.circled.adapter.viewholder.NewsTextViewBinder;
import com.rz.circled.adapter.viewholder.NewsUserViewBinder;
import com.rz.httpapi.bean.NewsBean;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by rzw2 on 2017/9/7.
 */

public class NewsMultiTypeAdapter extends MultiTypeAdapter {

    public NewsMultiTypeAdapter() {
        super();
        register();
    }

    private void register() {
        register(NewsBean.class).to(
                new NewsActivityViewBinder(),
                new NewsArticleViewBinder(),
                new NewsTextViewBinder(),
                new NewsAnnouncementViewBinder(),
                new NewsInteractiveViewBinder(),
                new NewsGroupViewBinder(),
                new NewsUserViewBinder()
        ).withClassLinker(new ClassLinker<NewsBean>() {
            @NonNull
            @Override
            public Class<? extends ItemViewBinder<NewsBean, ?>> index(@NonNull NewsBean data) {
                switch (data.getViewType()) {
                    case NewsBean.TYPE_TEXT:
                        return NewsTextViewBinder.class;
                    case NewsBean.TYPE_ACTIVITY:
                        return NewsActivityViewBinder.class;
                    case NewsBean.TYPE_ARTICLE:
                        return NewsArticleViewBinder.class;
                    case NewsBean.TYPE_USER:
                        return NewsUserViewBinder.class;
                    case NewsBean.TYPE_ANNOUNCEMENT:
                        return NewsAnnouncementViewBinder.class;
                    case NewsBean.TYPE_GROUP:
                        return NewsGroupViewBinder.class;
                    case NewsBean.TYPE_INTERACTIVE:
                        return NewsInteractiveViewBinder.class;
                    default:
                        return NewsTextViewBinder.class;
                }
            }
        });
    }
}
