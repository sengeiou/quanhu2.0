package com.rz.circled.ui.activity;


import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.PopupView;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.AudioUtils;
import com.rz.common.utils.Protect;

import butterknife.BindView;


/**
 * Created by xiayumo on 16/8/16.
 */
public class PersonScanAty extends BaseActivity implements View.OnLongClickListener, PopupView.OnItemPopupClick {

    @BindView(R.id.id_scan_photo)
    ImageView idScanPhoto;
    @BindView(R.id.id_scan_scan)
    ImageView idScanScan;
    @BindView(R.id.id_scan_nick)
    TextView idScanNick;
    @BindView(R.id.id_scan_area)
    TextView idScanArea;
    @BindView(R.id.id_root)
    ViewGroup root;

    private PopupWindow popWindow;

    AudioUtils audioUtils;
    PopupView popupView;

    public static final String[] POPUP_ITEMS = {"保存图片"};

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_my_scan, null);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.mine_person_scan));
        idScanNick.setText(Session.getUserName());
        if (TextUtils.isEmpty(Session.getUser_area())) {
//            idScanArea.setText(Session.getUser_area());
            idScanArea.setVisibility(View.GONE);
        } else {
            idScanArea.setText(Session.getUser_area());
        }
        if (Protect.checkLoadImageStatus(this)) {
            Glide.with(this).load(Session.getUserPicUrl()).transform(new GlideCircleImage(this)).placeholder(R.drawable.ic_default_head).into(idScanPhoto);
            Glide.with(this).load(Session.getUserLocalUrl()).placeholder(R.drawable.ic_height_bg).into(idScanScan);
        }
        idScanScan.setOnLongClickListener(this);
    }


    @Override
    public void initData() {
        audioUtils = new AudioUtils(this);
        popupView = new PopupView(this);
        popupView.setOnItemPopupClick(this);
    }

    @Override
    public boolean onLongClick(View v) {

        if (v.getId() == R.id.id_scan_scan) {
//            Intent intent = new Intent(getApplicationContext(), ShareNewsAty.class);
//            intent.putExtra(IntentKey.KEY_BOOLEAN, true);
//            intent.putExtra(IntentKey.KEY_TAG, true);
//            intent.putExtra(IntentKey.KEY_IMAGE, Session.getUserLocalUrl());
//            startActivity(intent);
            popupView.showAtLocPop(root, POPUP_ITEMS);
        }

        return false;
    }

    @Override
    public void OnItemClick(int position, String string) {
        switch (position) {

            case 0:
                idScanScan.buildDrawingCache();
                Bitmap bitmap = idScanScan.getDrawingCache();
                audioUtils.saveImageToGallery(bitmap);
                break;

            default:

                break;

        }

    }

    @Override
    public void refreshPage() {

    }
}
