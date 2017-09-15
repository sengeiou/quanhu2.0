package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.ImageAdaptationUtils;
import com.rz.httpapi.bean.CollectionBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class MyCollectionActivity extends BaseActivity {

    @BindView(R.id.lv_collection)
    ListView mLvCollection;
    private CirclePresenter mPresenter;
    List<CollectionBean> collectionList=new ArrayList<>();
    private BaseAdapter mCollectionAdapter;
    private ImageView mIv_edit;
    boolean isEdit=false;

    @Override
    protected View loadView(LayoutInflater inflater) {

        return inflater.inflate(R.layout.activity_my_collection, null);
    }

    @Override
    public void initView() {
        setTitleText("我的收藏");
        setTitleRightText("编辑");
        setTitleRightTextColor(R.color.color_0185FF);
        mLvCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CollectionBean collectionBean = collectionList.get(position);
                boolean isSelect = collectionBean.isSelect;
                if (isEdit){
                    if (isSelect) {
                        collectionBean.isSelect = false;
                    }else {
                        collectionBean.isSelect = true;
                    }
                    mCollectionAdapter.notifyDataSetChanged();

                }
            }
        });
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit){
                    //完成
                    isEdit=false;
                    setTitleRightText("编辑");
                    delCollection();
                }else {
                    //编辑
                    isEdit=true;
                    setTitleRightText("完成");
                }
                mCollectionAdapter.notifyDataSetChanged();

            }
        });

    }

    private void delCollection() {
        for (int i = 0; i < collectionList.size(); i++) {
            CollectionBean collectionBean = collectionList.get(i);
            if (collectionBean.isSelect){
               mPresenter.requestDeleteCollected(collectionBean.resourceInfo.getResourceId());
            }
        }
    }

    @Override
    public void initPresenter() {
        mPresenter = new CirclePresenter();
        mPresenter.attachView(this);
        mPresenter.getCircleCollection();
    }

    @Override
    public void initData() {
        //文章(1000)、话题(1001)、帖子(1002)、问题(1003)、答案(1004)、活动(1005)、悬赏(1006)
        mCollectionAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return collectionList==null?0:collectionList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CollectionBean collectionBean = collectionList.get(position);
                String resourceType = collectionBean.resourceInfo.getResourceType();
                //文章(1000)、话题(1001)、帖子(1002)、问题(1003)、答案(1004)、活动(1005)、悬赏(1006)
                if ("1003".equals(resourceType)||"1004".equals(resourceType)){
                    if (convertView==null){
                        convertView = View.inflate(mContext, R.layout.v3_item_circle_collect, null);
                    }


                }else {
                    if (convertView==null){
                        convertView = View.inflate(mContext, R.layout.v3_item_circle_collect, null);
                    }
                    ImageView iv_head = (ImageView) convertView.findViewById(R.id.civ_head);
                    ImageView only_iv = (ImageView) convertView.findViewById(R.id.iv_circle_1img);
                    LinearLayout iv_layout = (LinearLayout) convertView.findViewById(R.id.ll_circle_3imgs);
                    ImageView iv_1 = (ImageView) convertView.findViewById(R.id.iv_circle_img01);
                    ImageView iv_2 = (ImageView) convertView.findViewById(R.id.iv_circle_img02);
                    ImageView iv_3 = (ImageView) convertView.findViewById(R.id.iv_circle_img03);
                    ImageView mVideo_Preview = (ImageView) convertView.findViewById(R.id.iv_video_preview);
                    RelativeLayout rl_circle_video_content = (RelativeLayout) convertView.findViewById(R.id.rl_circle_video_content);
                    convertView.findViewById(R.id.iv_thumbnail).setVisibility(View.GONE);
                    mIv_edit = (ImageView) convertView.findViewById(R.id.iv_edit);
                    editCollection(mIv_edit,collectionBean);
                    TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    TextView title = (TextView) convertView.findViewById(R.id.tv_title);
                    TextView content = (TextView) convertView.findViewById(R.id.tv_des);
                    ViewGroup mImageLayout = (ViewGroup) convertView.findViewById(R.id.layout_image);
                    title.setText(collectionBean.resourceInfo.getTitle());
                    content.setText(collectionBean.resourceInfo.getContent());
                    Glide.with(mContext).load(collectionBean.user.getCustImg()).transform(new GlideCircleImage(mContext)).into(iv_head);
                    tv_name.setText(collectionBean.user.getCustNname());
                    //多图区域
                    String pics1 = collectionBean.resourceInfo.getPics();
                    if (TextUtils.isEmpty(pics1)) {
                        mImageLayout.setVisibility(View.GONE);
                    } else {
                        mImageLayout.setVisibility(View.VISIBLE);
                        String[] pics = pics1.split(",");
                        if (pics.length == 1) {
                            only_iv.setVisibility(View.VISIBLE);
                            iv_layout.setVisibility(View.GONE);
                            String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), pics1,R.dimen.px994,R.dimen.px558);
                            Glide.with(mContext).load(url).placeholder(R.drawable.ic_circle_img1).into(only_iv);
                        } else {
                            only_iv.setVisibility(View.GONE);
                            iv_layout.setVisibility(View.VISIBLE);
                            iv_1.setVisibility(View.INVISIBLE);
                            iv_2.setVisibility(View.INVISIBLE);
                            iv_3.setVisibility(View.INVISIBLE);
                            ImageView iv = null;
                            for (int i = 0; i < pics.length; i++) {
                                if (i >= 3) {
                                    break;
                                }
                                if (i == 0) {
                                    iv_1.setVisibility(View.VISIBLE);
                                    iv = iv_1;
                                } else if (i == 1) {
                                    iv_2.setVisibility(View.VISIBLE);
                                    iv = iv_2;
                                } else if (i == 2) {
                                    iv_3.setVisibility(View.VISIBLE);
                                    iv = iv_3;
                                }
                                String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), pics[i],R.dimen.px320,R.dimen.px320);
                                Glide.with(mContext).load(url).placeholder(R.drawable.ic_circle_img3).into(iv);
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(collectionBean.resourceInfo.getVideoPic())) {
                        rl_circle_video_content.setVisibility(View.VISIBLE);
                        iv_layout.setVisibility(View.GONE);
                        Glide.with(mContext).load(collectionBean.resourceInfo.getVideoPic()).placeholder(R.color.black).into(mVideo_Preview);
                    } else {
                        rl_circle_video_content.setVisibility(View.GONE);
                    }
                }
                return convertView;
            }
        };
        mLvCollection.setAdapter(mCollectionAdapter);

    }

    private void editCollection(ImageView iv_edit, CollectionBean collectionBean) {
        iv_edit.setVisibility(isEdit?View.VISIBLE:View.GONE);
        iv_edit.setImageResource(collectionBean.isSelect?R.drawable.edit_collection_yes:R.drawable.edit_collection_no);
    }

    @Override
    public <T> void updateView(T t) {
        if (t!=null)
        collectionList.clear();
        collectionList.addAll((Collection<? extends CollectionBean>) t);
        mCollectionAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
