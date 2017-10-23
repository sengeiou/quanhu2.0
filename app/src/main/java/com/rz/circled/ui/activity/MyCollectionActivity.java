package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.modle.ExjsonCollectionActivityProduct;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.GlideCenterRoundImage;
import com.rz.circled.widget.SwipyRefreshLayoutBanner;
import com.rz.common.constant.Constants;
import com.rz.common.swiperefresh.SwipyRefreshLayout;
import com.rz.common.swiperefresh.SwipyRefreshLayoutDirection;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.ImageAdaptationUtils;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.CollectionBean;
import com.rz.httpapi.bean.ExjsonCollection;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class MyCollectionActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.lv_collection)
    ListView mLvCollection;
    @BindView(R.id.refresh)
    SwipyRefreshLayoutBanner mRefresh;
    @BindView(R.id.btn_del)
    Button mBtnDel;
    @BindView(R.id.ll_edit)
    LinearLayout mLlEdit;
    private CirclePresenter mPresenter;
    List<CollectionBean> collectionList = new ArrayList<>();
    private BaseAdapter mCollectionAdapter;
    private ImageView mIv_edit;
    boolean isEdit = false;
    List<CollectionBean> cid = new ArrayList<>();

    @Override
    protected View loadView(LayoutInflater inflater) {

        return inflater.inflate(R.layout.activity_my_collection, null);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.my_collection));
        setTitleRightText(getString(R.string.edit));
        setTitleRightTextColor(R.color.color_0185FF);
        mRefresh.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mRefresh.setOnRefreshListener(this);
        mLvCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CollectionBean collectionBean = collectionList.get(position);
                boolean isSelect = collectionBean.isSelect;
                if (isEdit) {
                    if (isSelect) {
                        collectionBean.isSelect = false;
                        cid.remove(collectionBean);
                    } else {
                        cid.add(collectionBean);
                        collectionBean.isSelect = true;
                    }
                    mCollectionAdapter.notifyDataSetChanged();
                } else {
                    CollectionBean.CoterieInfoBean coterieInfo = collectionBean.coterieInfo;
                    CollectionBean.ResourceInfoBean resourceInfo = collectionBean.resourceInfo;
                    String resourceType = resourceInfo.getResourceType();
                    if ("1007".equals(resourceType)) {
                        String extjson = resourceInfo.extjson;
                        ExjsonCollectionActivityProduct product = gson.fromJson(extjson, ExjsonCollectionActivityProduct.class);
                        String productUrl = CommomUtils.getProductUrl(product.activityInfoId, product.id);
                        WebContainerActivity.startActivity(mContext, productUrl);
                        return;
                    }
                    if (StringUtils.isEmpty(coterieInfo.getCoterieId()) || StringUtils.isEmpty(coterieInfo.getName())) {
                        String circleUrl = CommomUtils.getCircleUrl(resourceInfo.getCircleRoute(), resourceInfo.getModuleEnum(), resourceInfo.getResourceId());
                        WebContainerActivity.startActivity(mContext, circleUrl);
                    } else {
                        String url = CommomUtils.getDymanicUrl(resourceInfo.getCircleRoute(), resourceInfo.getModuleEnum(), coterieInfo.getCoterieId(), resourceInfo.getResourceId());
                        WebContainerActivity.startActivity(mContext, url);
                    }

                }
            }
        });
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    //完成
                    isEdit = false;
                    setTitleRightText(getString(R.string.edit));
                    mLlEdit.setVisibility(View.GONE);
//                    mBtnDel.setVisibility(View.GONE);
                } else {
                    //编辑
                    isEdit = true;
                    mLlEdit.setVisibility(View.VISIBLE);
//                    mBtnDel.setVisibility(View.VISIBLE);
                    setTitleRightText(getString(R.string.finish));
                }
                mCollectionAdapter.notifyDataSetChanged();

            }
        });
        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delCollection();
            }
        });

    }

    private void delCollection() {
        for (CollectionBean cids : cid) {
            mPresenter.requestDeleteCollected(cids.cid);
        }
        collectionList.removeAll(cid);
        mCollectionAdapter.notifyDataSetChanged();

    }

    @Override
    public void initPresenter() {
        mPresenter = new CirclePresenter();
        mPresenter.attachView(this);
        mPresenter.getCircleCollection(false);
    }

    Gson gson = new Gson();

    @Override
    public void initData() {
        mCollectionAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return collectionList == null ? 0 : collectionList.size();
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
                final CollectionBean.UserBean user = collectionBean.user;
                final CollectionBean.CoterieInfoBean coterieInfo = collectionBean.coterieInfo;
                final CollectionBean.ResourceInfoBean resourceInfo = collectionBean.resourceInfo;
                String extjson = resourceInfo.extjson;
                ExjsonCollection exjson = gson.fromJson(extjson, ExjsonCollection.class);
                final ExjsonCollection.AnswerBean answer = exjson.answer;
                final ExjsonCollection.QuestionBean question = exjson.question;
                Log.i(TAG, "getView: " + extjson.toString());
                String resourceType = resourceInfo.getResourceType();
                //文章(1000)、话题(1001)、帖子(1002)、问题(1003)、答案(1004)、活动(1005)、悬赏(1006)活动作品(1007)
                //1.0版本收藏只有文章,帖子,活动,和问答
                if ("1003".equals(resourceType) || "1004".equals(resourceType)) {
                    viewHold vh = new viewHold();
                    convertView = null;
                    if (convertView == null) {
                        convertView = View.inflate(mContext, R.layout.answer_layout, null);
                        vh.answer_content = (TextView) convertView.findViewById(R.id.answer_content);
                        vh.answer_title = (TextView) convertView.findViewById(R.id.answer_title);
                        vh.answer_name = (TextView) convertView.findViewById(R.id.answer_name);
                        vh.question_name = (TextView) convertView.findViewById(R.id.question_name);
                        vh.audio_tv = (TextView) convertView.findViewById(R.id.audio_tv);
                        vh.answer_from = (TextView) convertView.findViewById(R.id.answer_from);
                        vh.iv_answer = (ImageView) convertView.findViewById(R.id.iv_answer);
                        vh.iv_edit = (ImageView) convertView.findViewById(R.id.iv_edit);
                        vh.ll_audio = (LinearLayout) convertView.findViewById(R.id.ll_audio);
                    }
                    editCollection(vh.iv_edit, collectionBean);
                    vh.question_name.setText(StringUtil.isEmpty(question.nickName) ? question.userName : question.nickName);
                    vh.answer_name.setText(StringUtil.isEmpty(question.targetNickName) ? question.targetUserName : question.targetNickName);
                    vh.answer_title.setText("Q:  " + question.content);
                    if (StringUtils.isEmpty(answer.content)) {
                        vh.answer_content.setVisibility(View.GONE);
                        vh.iv_answer.setVisibility(View.GONE);
                        vh.ll_audio.setVisibility(View.VISIBLE);
                        int audioLength = answer.audioLength / 1000;
                        vh.audio_tv.setText(+audioLength + "'");
                    } else {
                        if (!StringUtils.isEmpty(answer.imgUrl)) {
                            vh.iv_answer.setVisibility(View.VISIBLE);
                            String[] pics = answer.imgUrl.split(",");
                            Glide.with(mContext).load(pics[0]).placeholder(R.drawable.ic_circle_img1).transform(new GlideCenterRoundImage(mContext, 10)).into(vh.iv_answer);
                        } else {
                            vh.iv_answer.setVisibility(View.GONE);
                        }
                        vh.answer_content.setVisibility(View.VISIBLE);
                        vh.ll_audio.setVisibility(View.GONE);
                        vh.answer_content.setText("A:  " + answer.content);
                    }
                    vh.answer_from.setText(getString(R.string.from_coterie) + coterieInfo.getName());
                    vh.answer_from.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl + "/" + resourceInfo.getCircleRoute() + "/coterie/" + coterieInfo.getCoterieId());
                        }
                    });
                    vh.question_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfoActivity.newFrindInfo(mContext, question.custId);
                        }
                    });
                    vh.answer_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfoActivity.newFrindInfo(mContext, answer.custId);
                        }
                    });

                } else {
                    convertView = null;
                    if (convertView == null) {
                        convertView = View.inflate(mContext, R.layout.v3_item_circle_collect, null);
                    }
                    ImageView only_iv = (ImageView) convertView.findViewById(R.id.iv_circle_1img);
                    LinearLayout iv_layout = (LinearLayout) convertView.findViewById(R.id.ll_circle_3imgs);
                    ImageView iv_1 = (ImageView) convertView.findViewById(R.id.iv_circle_img01);
                    ImageView iv_2 = (ImageView) convertView.findViewById(R.id.iv_circle_img02);
                    ImageView iv_3 = (ImageView) convertView.findViewById(R.id.iv_circle_img03);
                    ImageView mVideo_Preview = (ImageView) convertView.findViewById(R.id.iv_video_preview);
                    RelativeLayout rl_circle_video_content = (RelativeLayout) convertView.findViewById(R.id.rl_circle_video_content);
                    mIv_edit = (ImageView) convertView.findViewById(R.id.iv_edit);
                    editCollection(mIv_edit, collectionBean);
                    TextView tv_name = (TextView) convertView.findViewById(R.id.tv_nick_name);
                    TextView fromWhere = (TextView) convertView.findViewById(R.id.tv_from_where);
                    TextView title = (TextView) convertView.findViewById(R.id.tv_title);
                    TextView content = (TextView) convertView.findViewById(R.id.tv_des);
                    ViewGroup mImageLayout = (ViewGroup) convertView.findViewById(R.id.layout_image);
                    fromWhere.setTextColor(getResources().getColor(R.color.color_999999));
                    //title是null说明是帖子
                    if (TextUtils.isEmpty(resourceInfo.getTitle())) {
                        content.setMaxLines(4);
                        title.setVisibility(View.GONE);
                    } else {
                        title.setText("1001".equals(resourceType) ? "#" + resourceInfo.getTitle() + "#" : resourceInfo.getTitle());
                        title.setVisibility(View.VISIBLE);
                    }
                    content.setText(resourceInfo.getContent());
                    tv_name.setText(user.getCustNname());
                    if ("1005".equals(resourceType) || "1007".equals(resourceType)) {
                        //如果是活动隐藏头部信息和脚部信息
                        fromWhere.setVisibility(View.GONE);
                        only_iv.setVisibility(View.VISIBLE);
                        iv_layout.setVisibility(View.GONE);
                        String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), resourceInfo.getThumnail(), R.dimen.px994, R.dimen.px558);
                        Glide.with(mContext).load(url).placeholder(R.drawable.ic_circle_img1).transform(new GlideCenterRoundImage(mContext, 10)).into(only_iv);
                    } else {
                        fromWhere.setVisibility(View.VISIBLE);
                        //多图区域
                        String pics1 = resourceInfo.getPics();
                        if (TextUtils.isEmpty(pics1)) {
                            mImageLayout.setVisibility(View.GONE);
                        } else {
                            mImageLayout.setVisibility(View.VISIBLE);
                            String[] pics = pics1.split(",");
                            if (pics.length == 1) {
                                only_iv.setVisibility(View.VISIBLE);
                                iv_layout.setVisibility(View.GONE);
                                String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), pics1, R.dimen.px994, R.dimen.px558);
                                Glide.with(mContext).load(url).placeholder(R.drawable.ic_circle_img1).transform(new GlideCenterRoundImage(mContext, 10)).into(only_iv);
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
                                    String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), pics[i], R.dimen.px320, R.dimen.px320);
                                    Glide.with(mContext).load(url).placeholder(R.drawable.ic_circle_img3).transform(new GlideCenterRoundImage(mContext, 10)).into(iv);
                                }
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(resourceInfo.getVideoPic())) {
                        rl_circle_video_content.setVisibility(View.VISIBLE);
                        iv_layout.setVisibility(View.GONE);
                        Glide.with(mContext).load(resourceInfo.getVideoPic()).placeholder(R.color.black).transform(new GlideCenterRoundImage(mContext, 10)).into(mVideo_Preview);
                    } else {
                        rl_circle_video_content.setVisibility(View.GONE);
                    }
                    if (StringUtils.isEmpty(coterieInfo.getCoterieId()) || StringUtils.isEmpty(coterieInfo.getName())) {
                        fromWhere.setText(getString(R.string.from_circle) + collectionBean.circleInfo.getCircleName());
                    } else {
                        fromWhere.setText(getString(R.string.from_coterie) + coterieInfo.getName());
                    }
                    tv_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfoActivity.newFrindInfo(mContext, user.getCustId());
                        }
                    });
                    fromWhere.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (StringUtil.isEmpty(coterieInfo.getCoterieId()) || StringUtil.isEmpty(coterieInfo.getName())) {
                                WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl + "/" + resourceInfo.getCircleRoute() + "/");
                            } else {
                                WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl + "/" + resourceInfo.getCircleRoute() + "/coterie/" + coterieInfo.getCoterieId());
                            }
                        }
                    });
                }
                return convertView;
            }
        };
        mLvCollection.setAdapter(mCollectionAdapter);

    }

    @Override
    public void refreshPage() {
        mPresenter.getCircleCollection(false);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        mPresenter.getCircleCollection(direction != SwipyRefreshLayoutDirection.TOP);
        mRefresh.setRefreshing(false);
    }

    class viewHold {
        TextView question_name;
        TextView answer_name;
        TextView answer_title;
        TextView audio_tv;
        TextView answer_content;
        TextView answer_from;
        ImageView iv_answer;
        ImageView iv_edit;
        LinearLayout ll_audio;
    }

    private void editCollection(ImageView iv_edit, CollectionBean collectionBean) {
        iv_edit.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        iv_edit.setImageResource(collectionBean.isSelect ? R.drawable.edit_collection_yes : R.drawable.edit_collection_no);
    }

    @Override
    public <T> void updateView(T t) {
        if (t != null)
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


    @Override
    protected boolean hasDataInPage() {
        return mCollectionAdapter != null && mCollectionAdapter.getCount() != 0;
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }
}
