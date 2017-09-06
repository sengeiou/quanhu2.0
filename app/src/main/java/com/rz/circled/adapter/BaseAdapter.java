package com.rz.circled.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.rz.circled.R;
import com.rz.common.utils.DensityUtils;
import com.rz.common.widget.svp.SVProgressHUD;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共adapter
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    private List<T> list;

    public Dialog mDialog;

    public View mBaseView;

    protected Context context;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    /**
     * <默认构函数> 因为涉及到theme，布局要当前activity的content，所以强制传Context
     */
    private BaseAdapter() {
        init(null, new ArrayList<T>());
    }

    public BaseAdapter(List<T> list) {
        init(null, list);
    }

    public BaseAdapter(Context context) {
        init(context, new ArrayList<T>());
    }

    public BaseAdapter(Context context, List<T> list) {
        init(context, list);
    }

    private void init(Context context, List<T> list) {
        this.list = list;
        this.context = context;
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        if (getCount() > 0) {
            return list.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 加载布局，推荐使用inflate(int layoutResID, ViewGroup root)z
     *
     * @param layoutResID
     * @return View [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @deprecated
     */
    protected View inflate(int layoutResID) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layoutResID, null);
        return view;
    }

    protected View inflate(int layoutResID, ViewGroup root) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layoutResID, root, false);
        return view;
    }

    public void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public void showToastById(int resouce) {
        Toast.makeText(context, context.getString(resouce), Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * 弹出框消失
     */
    public void SVProgressHUDDismiss() {
        if (SVProgressHUD.isShowing(context)) {
            SVProgressHUD.dismiss(context);
        }
    }

    /**
     * 自定义弹出框
     *
     * @param resource
     * @param flag     点击屏幕外面是否可以消失
     */
    public void selfDialog(int resource, boolean flag) {
        mBaseView = LayoutInflater.from(context).inflate(resource, null);
        if (mDialog != null) {
            dialogDisMiss();
        }
        mDialog = new Dialog(context, R.style.AppTheme_DialogStyle);
        LayoutParams params = new LayoutParams(
                DensityUtils.getScreenW(context) - 80,
                LayoutParams.WRAP_CONTENT);
        mDialog.setContentView(mBaseView, params);
        mDialog.setCanceledOnTouchOutside(flag);
    }

    /**
     * 取消dialog
     */
    public void dialogDisMiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /**
     * 展示dialog
     */
    public void dialogShow() {
        if (mDialog != null) {
            mDialog.show();
        }
    }
}
