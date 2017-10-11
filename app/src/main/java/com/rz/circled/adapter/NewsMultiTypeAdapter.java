package com.rz.circled.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.rz.circled.adapter.viewholder.NewsActivityViewBinder;
import com.rz.circled.adapter.viewholder.NewsAnnouncementViewBinder;
import com.rz.circled.adapter.viewholder.NewsArticleViewBinder;
import com.rz.circled.adapter.viewholder.NewsGroupViewBinder;
import com.rz.circled.adapter.viewholder.NewsInteractiveViewBinder;
import com.rz.circled.adapter.viewholder.NewsTextViewBinder;
import com.rz.circled.adapter.viewholder.NewsUserViewBinder;
import com.rz.circled.constants.NewsTypeConstants;
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
                switch (Integer.parseInt(data.getViewCode())) {
                    case NewsTypeConstants.TYPE_ANNOUNCEMENT:
                        return NewsAnnouncementViewBinder.class;
                    case NewsTypeConstants.TYPE_SAFE_1:
                        return NewsTextViewBinder.class;
                    case NewsTypeConstants.TYPE_SAFE_2:
                        return NewsArticleViewBinder.class;
                    case NewsTypeConstants.TYPE_SYSTEM_1:
                        return NewsTextViewBinder.class;
                    case NewsTypeConstants.TYPE_SYSTEM_2:
                        return NewsArticleViewBinder.class;
                    case NewsTypeConstants.TYPE_RECOMMEND_1:
                        return NewsActivityViewBinder.class;
                    case NewsTypeConstants.TYPE_RECOMMEND_2:
                        return NewsUserViewBinder.class;
                    case NewsTypeConstants.TYPE_RECOMMEND_3:
                        return NewsGroupViewBinder.class;
                    case NewsTypeConstants.TYPE_INTERACTIVE:
                        return NewsInteractiveViewBinder.class;
                    default:
                        return NewsTextViewBinder.class;
                }
            }
        });
    }
}
