package com.rz.common.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rz.common.R;
import com.rz.common.utils.DialogUtils;


/**
 * Dialog to prompt the user to go to the app's settings screen and enable permissions. If the
 * user clicks 'OK' on the dialog, they are sent to the settings screen. The result is returned
 * to the Activity via {@link Activity#onActivityResult(int, int, Intent)}.
 * <p/>
 * Use {@link Builder} to create and display a dialog.
 */
public class AppSettingsDialog {

    public static final int DEFAULT_SETTINGS_REQ_CODE = 16061;

    private AlertDialog mAlertDialog;
    private Dialog dialog;

    private AppSettingsDialog(@NonNull final Object activityOrFragment,
                              @NonNull final Context context,
                              @NonNull String rationale,
                              @Nullable String title,
                              @Nullable String positiveButton,
                              @Nullable String negativeButton,
                              @Nullable DialogInterface.OnClickListener negativeListener,
                              int requestCode) {
        final int settingsRequestCode = requestCode > 0 ? requestCode : DEFAULT_SETTINGS_REQ_CODE;
        View view = ((LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_permission, null, false);
        TextView tv = (TextView) view.findViewById(R.id.id_tv_title);
        tv.setText(rationale);
        if (!TextUtils.isEmpty(rationale)) {
            rationale.replaceAll("否则app可能无法正常运行", "");
        }
        view.findViewById(R.id.id_tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //                // Create app settings intent
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);

                // Start for result
                startForResult(activityOrFragment, intent, settingsRequestCode);
            }
        });

        dialog = DialogUtils.selfDialog(context, view, true);
        dialog.show();
    }

    @TargetApi(11)
    private void startForResult(Object object, Intent intent, int requestCode) {
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * Display the built dialog.
     */
    public void show() {
//        mAlertDialog.show();
        dialog.show();
    }

    /**
     */
    public static class Builder {

        private Object mActivityOrFragment;
        private Context mContext;
        private String mRationale;
        private String mTitle;
        private String mPositiveButton;
        private String mNegativeButton;
        private DialogInterface.OnClickListener mNegativeListener;
        private int mRequestCode = -1;

        /**
         * @param activity  the Activity in which to display the dialog.
         * @param rationale text explaining why the user should launch the app settings screen.
         */
        public Builder(@NonNull Activity activity, @NonNull String rationale) {
            mActivityOrFragment = activity;
            mContext = activity;
            mRationale = rationale;
        }

        /**
         * @param fragment  the Fragment in which to display the dialog.
         * @param rationale text explaining why the user should launch the app settings screen.
         */
        public Builder(@NonNull Fragment fragment, @NonNull String rationale) {
            mActivityOrFragment = fragment;
            mContext = fragment.getContext();
            mRationale = rationale;
        }

        /**
         * @param fragment  the Fragment in which to display the dialog.
         * @param rationale text explaining why the user should launch the app settings screen.
         */
        @TargetApi(11)
        public Builder(@NonNull android.app.Fragment fragment, @NonNull String rationale) {
            mActivityOrFragment = fragment;
            mContext = fragment.getActivity();
            mRationale = rationale;
        }


        /**
         * Set the title dialog. Default is no title.
         */
        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        /**
         * Set the positive button text, default is {@code android.R.string.ok}.
         */
        public Builder setPositiveButton(String positiveButton) {
            mPositiveButton = positiveButton;
            return this;
        }

        /**
         * Set the negative button text and click listener, default text is
         * {@code android.R.string.cancel}.
         */
        public Builder setNegativeButton(String negativeButton,
                                         DialogInterface.OnClickListener negativeListener) {
            mNegativeButton = negativeButton;
            mNegativeListener = negativeListener;
            return this;
        }

        /**
         * Set the request code use when launching the Settings screen for result, can be
         * retrieved in the calling Activity's {@code onActivityResult} method. Default is
         * {@link #DEFAULT_SETTINGS_REQ_CODE}.
         */
        public Builder setRequestCode(int requestCode) {
            mRequestCode = requestCode;
            return this;
        }

        /**
         */
        public AppSettingsDialog build() {
            return new AppSettingsDialog(mActivityOrFragment, mContext, mRationale, mTitle,
                    mPositiveButton, mNegativeButton, mNegativeListener, mRequestCode);
        }

    }

}
