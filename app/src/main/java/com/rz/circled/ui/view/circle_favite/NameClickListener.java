package com.rz.circled.ui.view.circle_favite;


import android.text.SpannableString;


/**
 * @author wsf
 * @Description:
 * @date 16/3/10 16:54
 */
public class NameClickListener implements ISpanClick {
    private SpannableString userName;
    private String userId;

    public NameClickListener(SpannableString name, String userid) {
        this.userName = name;
        this.userId = userid;
    }

    @Override
    public void onClick(int position) {
     //   Toast.makeText(App.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
    }
}
