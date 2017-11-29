package com.rz.circled.widget.photoview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rz.circled.R;

/**
 * @author wsf
 * @ClassName: PicSaveDialog
 * @Description: 评论长按对话框，保护复制和删除
 * @date 2016-3-12
 */
public class PicSaveDialog extends Dialog implements
        View.OnClickListener {

    private Context mContext;
    private IPhotoSaveListener mPhotoSaveListener;

    public PicSaveDialog(Context context, IPhotoSaveListener presenter
    ) {
        super(context, R.style.ActionSheetDialogStyle);
        mContext = context;
        this.mPhotoSaveListener = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_picsave);
        initWindowParams();
        initView();
    }

    private void initWindowParams() {
        Window dialogWindow = getWindow();
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        TextView copyTv = (TextView) findViewById(R.id.copy_tv);
        copyTv.setOnClickListener(this);
        TextView cancelTv = (TextView) findViewById(R.id.cancel_btn);
        cancelTv.setOnClickListener(this);

        /*
		 * if (mCommentItem != null && DatasUtil.curUser.getId().equals(
		 * mCommentItem.getUser().getId())) {
		 * deleteTv.setVisibility(View.VISIBLE); } else {
		 * deleteTv.setVisibility(View.GONE); }
		 */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copy_tv:
                mPhotoSaveListener.onPhotoSave();
                dismiss();
                break;
            case R.id.cancel_btn:
                dismiss();
                break;
            default:
                break;
        }
    }

}
