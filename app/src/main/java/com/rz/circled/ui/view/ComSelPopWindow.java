package com.rz.circled.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.rz.circled.R;
import com.rz.circled.adapter.PicDirPopupAdapter;
import com.rz.common.utils.DensityUtils;
import com.rz.httpapi.bean.ImageFloder;

import java.util.ArrayList;
import java.util.List;

/**
 * popupWindow弹出框
 */
public class ComSelPopWindow extends PopupWindow implements OnItemClickListener {

	/** 文件夹信息 */
	private List<ImageFloder> mFloders = new ArrayList<ImageFloder>();

	private PicDirPopupAdapter mPicDirPopup;

	public ComSelPopWindow(Context mContext) {
		View mView = LayoutInflater.from(mContext).inflate(R.layout.popup_list_folder, null);
		ListView mListView = (ListView) mView.findViewById(R.id.id_folder_list);

		this.setContentView(mView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight((int) (DensityUtils.getScreenH(mContext) * 0.7));
		setTouchable(true);
		setFocusable(true);
		setOutsideTouchable(true);
		setAnimationStyle(R.style.anim_popup_dir);
		setBackgroundDrawable(new ColorDrawable(0000000000));

		mPicDirPopup = new PicDirPopupAdapter(mContext, R.layout.layout_pic_dir);
		mListView.setAdapter(mPicDirPopup);
		mListView.setOnItemClickListener(this);

		setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					dismiss();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 显示弹出框
	 * 
	 * @param mLocation
	 *            哪个控件上弹出
	 * @param floders
	 *            数据源
	 */
	public void showPop(View mLocation, List<ImageFloder> floders) {
		mFloders.clear();
		mFloders.addAll(floders);
		mPicDirPopup.setData(mFloders);
		mPicDirPopup.notifyDataSetChanged();
		showAsDropDown(mLocation, 0, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if (null != mListener) {
			mListener.onSelect(position, mPicDirPopup.getItem(position));
		}
	}

	public onSelectFloderListener mListener;

	public interface onSelectFloderListener {
		public void onSelect(int position, ImageFloder imageFloder);
	}

	public void setOnSelectFloderListener(onSelectFloderListener mListener) {
		this.mListener = mListener;
	}
}
