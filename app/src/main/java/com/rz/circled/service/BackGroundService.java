package com.rz.circled.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;

import com.rz.common.cache.preference.EntityCache;
import com.rz.common.utils.NetUtils;
import com.rz.httpapi.bean.FriendInformationBean;

import java.util.ArrayList;
import java.util.List;

public class BackGroundService extends IntentService {

    /**
     * 验证码时间
     */
    public static long time_code = 0;

    public static MyCountCode countCode;

    public BackGroundService() {
        super("BackGroundService");
    }

    @Override
    protected void onHandleIntent(Intent data) {
    }

    /**
     * 倒计时类
     */
    public static class MyCountCode extends CountDownTimer {

        public MyCountCode(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            time_code = millisUntilFinished;
        }

        @Override
        public void onFinish() {
            time_code = 0;
        }
    }

    /**
     * 倒计时
     *
     * @param time
     * @return
     */
    public static void countDownCode(long time) {
        if (null != countCode) {
            countCode.cancel();
            countCode = null;
        }
        countCode = new MyCountCode(time, 1000);
        countCode.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != countCode) {
            countCode.cancel();
        }
    }

    private static List<FriendInformationBean> mSaveAllFriends = new ArrayList<>();


    private static final String[] PHONES_PROJECTION = new String[]{
            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID};

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;
    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    //处理缓存
    private static EntityCache<FriendInformationBean> mBaseInfoCache;

    /**
     * 得到手机通讯录联系人信息
     **/
    public static void getPhoneContacts(Context mContext) {

        mBaseInfoCache = new EntityCache<FriendInformationBean>(mContext, FriendInformationBean.class);
        ContentResolver resolver = mContext.getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            mSaveAllFriends.clear();
            while (phoneCursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                FriendInformationBean mFriendBean = new FriendInformationBean();
                mFriendBean.setCustPhone(phoneNumber);

                mSaveAllFriends.add(mFriendBean);

                //得到联系人名称
//                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                //得到联系人ID
//                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                //得到联系人头像ID
//                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                //得到联系人头像Bitamp
//                Bitmap contactPhoto = null;
                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
//                if (photoid > 0) {
//                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
//                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
//                    contactPhoto = BitmapFactory.decodeStream(input);
//                } else {
//                    contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.contact_photo);
//                }
            }
            phoneCursor.close();
        }
        if (null != mSaveAllFriends && !mSaveAllFriends.isEmpty()) {
            try {
                mBaseInfoCache.putListEntity(mSaveAllFriends);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        loadFriends(mContext);
    }

    public static void loadFriends(Context mContext) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            return;
        }
        String phone = "";
        if (mSaveAllFriends != null && !mSaveAllFriends.isEmpty()) {
            try {
                for (int i = 0; i < mSaveAllFriends.size(); i++) {
                    phone += mSaveAllFriends.get(i).getCustPhone() + ",";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
