package com.rz.circled.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Currency;
import com.rz.httpapi.bean.OpusPriceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：Administrator on 2016/8/18 0018 17:35
 * 功能：通用的popopwindow
 * 说明：
 */
public class PopupView implements AdapterView.OnItemClickListener, View.OnClickListener {

    //上下文
    private Context mContext;

    private PopupWindow popWindow;

    private LayoutInflater mInflater;

    //内容布局
    private View viewContent;

    private ListView mListView;

    private CommonAdapter<String> mAdapter;

    //存储数据
    private List<String> mItems = new ArrayList<String>();

    //gridView布局
    private View mViewGridContent;

    private GridView mGridView;

    private PriceAdapter mPriceAdapter;

    //记录点击的下标
    private int mRecordPosition = 0;

    //存储数据
    private List<OpusPriceModel> mPrices = new ArrayList<OpusPriceModel>();

    private String mTitle;


    //存储item的颜色
    private String[] mItemColor = null;

    //弹出框初始化
    public PopupView(Context mContext) {
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewContent = mInflater.inflate(R.layout.view_popup_window, null);
        mListView = (ListView) viewContent.findViewById(R.id.id_popup_listview);
        viewContent.findViewById(R.id.id_popup_cancel_btn).setOnClickListener(this);
        mAdapter = new CommonAdapter<String>(mContext, R.layout.popup_textview) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                TextView mTxt = (TextView) helper.getViewById(R.id.id_popup_txt);
                mTxt.setText(item);
                if (null != mItemColor) {
                    mTxt.setTextColor(Color.parseColor(mItemColor[helper.getPosition()]));
                }
            }
        };
        mListView.setAdapter(mAdapter);
        mAdapter.setData(mItems);
        mListView.setOnItemClickListener(this);
    }

    //弹出框初始化
    public PopupView(Context mContext, String str) {
        this.mContext = mContext;
        this.mTitle = str;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewGridContent = mInflater.inflate(R.layout.popup_price, null);
        mGridView = (GridView) mViewGridContent.findViewById(R.id.id_opus_price_gridv);
        mViewGridContent.findViewById(R.id.id_close_img).setOnClickListener(this);

        mPriceAdapter = new PriceAdapter(mContext, R.layout.item_price);
        mGridView.setAdapter(mPriceAdapter);
        mPriceAdapter.setData(mPrices);
        mGridView.setOnItemClickListener(this);
    }


    public class PriceAdapter extends CommonAdapter {

        public SparseBooleanArray checkMap;
        /**
         * 用来记录checkbox的状态
         */
        private boolean checkedDefaultValue = false;

        /**
         * 改变某个item的选中状态 <功能详细描述> 单选
         */
        public void setCheckAtPosFalse(int pos, boolean flag) {
            for (int i = 0; i < getCount(); i++) {
                checkMap.put(i, false);
            }
            if (flag) {
                checkMap.put(pos, !checkMap.get(pos, checkedDefaultValue));
            }
            notifyDataSetChanged();
        }

        public PriceAdapter(Context context, int layoutId) {
            super(context, layoutId);
            if (checkMap == null)
                checkMap = new SparseBooleanArray();
        }

        @Override
        public void convert(ViewHolder helper, Object item, int position) {
            OpusPriceModel priceModel = (OpusPriceModel) item;
            TextView mTxt = (TextView) helper.getViewById(R.id.id_popup_price_txt);
            LinearLayout mLinear = (LinearLayout) helper.getViewById(R.id.id_no_all_ll);
            TextView mTxtPrice1 = (TextView) helper.getViewById(R.id.id_txt_1);
            TextView mTxtPrice = (TextView) helper.getViewById(R.id.id_price_txt);
            TextView mTxtPrice2 = (TextView) helper.getViewById(R.id.id_txt_2);

            if (priceModel.isShowAllBtn()) {
                mTxt.setVisibility(View.VISIBLE);
                mLinear.setVisibility(View.GONE);
            } else {
                mTxt.setVisibility(View.GONE);
                mLinear.setVisibility(View.VISIBLE);
                mTxtPrice.setText(Currency.returnDollar(priceModel.getPrice()).replace(mContext.getString(R.string.yuan), "").trim());
            }

            if (checkMap.get(helper.getPosition(), false)) {
                if (helper.getPosition() == 0) {
                    mTxt.setVisibility(View.VISIBLE);
                    mLinear.setVisibility(View.GONE);
                    mTxt.setSelected(true);
                    mTxt.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    mTxt.setVisibility(View.GONE);
                    mLinear.setVisibility(View.VISIBLE);
                    mLinear.setSelected(true);
                    mTxtPrice1.setSelected(true);
                    mTxtPrice2.setSelected(true);
                    mTxtPrice.setSelected(true);
                    mTxtPrice1.setTextColor(Color.parseColor("#ffffff"));
                    mTxtPrice.setTextColor(Color.parseColor("#ffffff"));
                    mTxtPrice2.setTextColor(Color.parseColor("#ffffff"));
                }
            } else {
                if (helper.getPosition() == 0) {
                    mTxt.setVisibility(View.VISIBLE);
                    mLinear.setVisibility(View.GONE);
                    mTxt.setSelected(false);
                    mTxt.setTextColor(Color.parseColor("#666666"));
                } else {
                    mTxt.setVisibility(View.GONE);
                    mLinear.setVisibility(View.VISIBLE);
                    mLinear.setSelected(false);
                    mTxtPrice1.setSelected(false);
                    mTxtPrice2.setSelected(false);
                    mTxtPrice.setSelected(false);
                    mTxtPrice1.setTextColor(Color.parseColor("#666666"));
                    mTxtPrice.setTextColor(Color.parseColor("#ff6600"));
                    mTxtPrice2.setTextColor(Color.parseColor("#666666"));
                }
            }
        }
    }

    /**
     * 设置item的颜色
     */
    public void setItemColor(String[] colors) {
        this.mItemColor = colors;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 展示popupwindow
     *
     * @param root  展示层
     * @param items 数据
     */
    public void showAtLocPop(View root, String[] items) {
        if (null != popWindow && popWindow.isShowing()) {
            return;
        }
        mItems.clear();
        mItems.addAll(Arrays.asList(items));
        mAdapter.setData(mItems);
        mAdapter.notifyDataSetChanged();

        popWindow = new PopupWindow(viewContent, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.setAnimationStyle(R.style.popwindow_anim_style);
        popWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
    }

    public void showAtLocPopCenter(View root, String[] items) {
        if (null != popWindow && popWindow.isShowing()) {
            return;
        }
        mItems.clear();
        mItems.addAll(Arrays.asList(items));
        mAdapter.setData(mItems);
        mAdapter.notifyDataSetChanged();

        popWindow = new PopupWindow(viewContent, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.setAnimationStyle(R.style.popwindow_anim_style);
        popWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
    }

    /**
     * 展示popupwindow,从上往下
     */
    public void showAtTopPop(View root, List<OpusPriceModel> items) {
        if (null != popWindow && popWindow.isShowing()) {
            return;
        }
        mPrices.clear();
        mPrices.addAll(items);
//        mPriceAdapter.setCheckAtPosFalse(mRecordPosition, true);
        mPriceAdapter.setData(mPrices);

        popWindow = new PopupWindow(mViewGridContent, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.setAnimationStyle(R.style.popwindow_anim_style);

        popWindow.showAsDropDown(root);
//        popWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 展示popupwindow,从上往下
     */
    public void showAtTopPopCenter(View root, List<OpusPriceModel> items) {
        if (null != popWindow && popWindow.isShowing()) {
            return;
        }
        mPrices.clear();
        mPrices.addAll(items);
        mPriceAdapter.setData(mPrices);
        mPriceAdapter.setCheckAtPosFalse(mRecordPosition, true);
//        mPriceAdapter.notifyDataSetChanged();

        popWindow = new PopupWindow(mViewGridContent, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.setAnimationStyle(R.style.popwindow_anim_style);

        popWindow.showAsDropDown(root);
        popWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            //
            case R.id.id_popup_listview:
                mListener.OnItemClick(i, "");
                popWindow.dismiss();
                break;
            case R.id.id_opus_price_gridv:
                if (null != mPriceAdapter) {
                    mRecordPosition = i;
                    mPriceAdapter.setCheckAtPosFalse(i, true);
//                    mPriceAdapter.notifyDataSetChanged();
                    mListener.OnItemClick(i, ((OpusPriceModel) mPriceAdapter.getItem(i)).getPrice());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            popWindow.dismiss();
                        }
                    }, 500);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_popup_cancel_btn:
                popWindow.dismiss();
                break;
            case R.id.id_close_img:
                popWindow.dismiss();
                break;
        }
    }

    private OnItemPopupClick mListener;

    public interface OnItemPopupClick {
        void OnItemClick(int position, String tag);
    }

    public void setOnItemPopupClick(OnItemPopupClick mListener) {
        this.mListener = mListener;
    }
}
