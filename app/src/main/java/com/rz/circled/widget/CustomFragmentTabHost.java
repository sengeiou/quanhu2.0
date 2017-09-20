package com.rz.circled.widget;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;


public class CustomFragmentTabHost extends TabHost implements TabHost.OnTabChangeListener {
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private FrameLayout mRealTabContent;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private TabHost.OnTabChangeListener mOnTabChangeListener;
    private TabInfo mLastTab;
    private boolean mAttached;

    public InterceptTagChanged interceptTagChanged;

    public interface InterceptTagChanged {
        boolean intercept(String tabId);
    }

    public InterceptTagChanged getInterceptTagChanged() {
        return interceptTagChanged;
    }

    public void setInterceptTagChanged(InterceptTagChanged interceptTagChanged) {
        this.interceptTagChanged = interceptTagChanged;
    }

    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int layoutHightPix = 0;
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (layoutWidth <= 1080) {
            layoutHightPix = (int) (layoutWidth * (140) / 1080) + 5;
        } else if (layoutWidth <= 720) {
            layoutHightPix = (int) (layoutWidth * (140) / 1080) + 5;// UI效果图
        } else {
            float ration = ((float) layoutHightPix) / layoutWidth;
            if (ration <= 4.1f / 3) {
                layoutHightPix = (int) (layoutWidth * (140) / 1080);
            } else {
                layoutHightPix = (int) (layoutWidth * (140) / 1080) + 5;// UI效果图
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(layoutHightPix, mode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    static class SavedState extends BaseSavedState {
        String curTab;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            curTab = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(curTab);
        }

        @Override
        public String toString() {
            return "FragmentTabHost.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " curTab="
                    + curTab + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public CustomFragmentTabHost(Context context) {
        // Note that we call through to the version that takes an AttributeSet,
        // because the simple Context construct can result in a broken object!
        super(context, null);
        initFragmentTabHost(context, null);
    }

    public CustomFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFragmentTabHost(context, attrs);
    }

    private void initFragmentTabHost(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.inflatedId}, 0, 0);
        mContainerId = a.getResourceId(0, 0);
        a.recycle();

        super.setOnTabChangedListener(this);

        // If owner hasn't made its own view hierarchy, then as a convenience
        // we will construct a standard one here.
        if (findViewById(android.R.id.tabs) == null) {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.VERTICAL);
            addView(ll, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            TabWidget tw = new TabWidget(context);
            // tw.setShowDividers(TabWidget.SHOW_DIVIDER_NONE);
            tw.setDividerDrawable(null);
            tw.setId(android.R.id.tabs);
            tw.setOrientation(TabWidget.HORIZONTAL);
            ll.addView(tw, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 0));

            FrameLayout fl = new FrameLayout(context);
            fl.setId(android.R.id.tabcontent);
            ll.addView(fl, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0));

            mRealTabContent = fl = new FrameLayout(context);
            mRealTabContent.setId(mContainerId);
            ll.addView(mRealTabContent, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1));
        }
    }

    public void setup(Context context, FragmentManager manager) {
        super.setup();
        mContext = context;
        mFragmentManager = manager;
        ensureContent();
    }


    public void setup(Context context, FragmentManager manager, int containerId) {
        super.setup();
        mContext = context;
        mFragmentManager = manager;
        mContainerId = containerId;
        ensureContent();
        mRealTabContent.setId(containerId);

        // We must have an ID to be able to save/restore our state. If
        // the owner hasn't set one at this point, we will set it ourself.
        if (getId() == View.NO_ID) {
            setId(android.R.id.tabhost);
        }
    }

    private void ensureContent() {
        if (mRealTabContent == null) {
            mRealTabContent = (FrameLayout) findViewById(mContainerId);
            if (mRealTabContent == null) {
                throw new IllegalStateException("No tab content FrameLayout found for id " + mContainerId);
            }
        }
    }

    @Override
    public void setOnTabChangedListener(OnTabChangeListener l) {
        mOnTabChangeListener = l;
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mContext));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);

        if (mAttached) {
            // If we are already attached to the window, then check to make
            // sure this tab's fragment is inactive if it exists. This shouldn't
            // normally happen.
            info.fragment = mFragmentManager.findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }
        }

        mTabs.add(info);
        addTab(tabSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        String currentTab = getCurrentTabTag();

        // Go through all tabs and make sure their fragments match
        // the correct state.
        FragmentTransaction ft = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
            if (tab.fragment != null && !tab.fragment.isHidden()) {
                if (tab.tag.equals(currentTab)) {
                    // The fragment for this tab is already there and
                    // active, and it is what we really want to have
                    // as the current tab. Nothing to do.
                    mLastTab = tab;
                } else {
                    // This fragment was restored in the active state,
                    // but is not the current tab. Deactivate it.
                    if (ft == null) {
                        ft = mFragmentManager.beginTransaction();
                    }
                    ft.hide(tab.fragment);
                }
            }
        }

        // We are now ready to go. Make sure we are switched to the
        // correct tab.
        mAttached = true;
        ft = doTabChanged(currentTab, ft);
        if (ft != null) {
            ft.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.curTab = getCurrentTabTag();
        Log.e("zxw", "onSaveInstanceState: " + ss.curTab);
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentTabByTag(ss.curTab);
        Log.e("zxw", "onRestoreInstanceState: " + ss.curTab);
    }

    @Override
    public void onTabChanged(String tabId) {
        if (interceptTagChanged != null && interceptTagChanged.intercept(tabId) && mLastTab != null) {
            int lastTabPos = 0;
            for (int i = 0; i < mTabs.size(); i++) {
                TabInfo tab = mTabs.get(i);
                if (tab.tag.equals(mLastTab.tag)) {
                    lastTabPos = i;
                    break;
                }
            }
            setCurrentTab(lastTabPos);
            return;
        }
        Log.i("doTabChanged", "------->onTabChanged " + tabId);
        if (mAttached) {
            Log.d("Fragment", "onTabChanged  doTabChanged");
            FragmentTransaction ft = doTabChanged(tabId, null);
            if (ft != null) {
                ft.commitAllowingStateLoss();
                // ft.commit();
            }
        }
        if (mOnTabChangeListener != null) {
            mOnTabChangeListener.onTabChanged(tabId);
        }
    }

    private FragmentTransaction doTabRestore(String tabId, FragmentTransaction ft) {

        TabInfo newTab = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.hide(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(mContext, newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    ft.show(newTab.fragment);
                }
            }

            mLastTab = newTab;
        }
        return ft;
    }

    private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabInfo newTab = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.hide(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(mContext, newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    ft.show(newTab.fragment);
                }
            }

            mLastTab = newTab;
        }
        return ft;
    }

    @Override
    public TabSpec newTabSpec(String tag) {
        Log.e("zxw", "newTabSpec: " + tag);
        return super.newTabSpec(tag);
    }

    @Override
    public void setup() {
        super.setup();
        Log.e("zxw", "setup: ");
    }

    @Override
    public void setup(LocalActivityManager activityGroup) {
        Log.e("zxw", "setup: " + activityGroup.toString());
        super.setup(activityGroup);
    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        Log.e("zxw", "onTouchModeChanged: " + isInTouchMode);
        super.onTouchModeChanged(isInTouchMode);
    }

    @Override
    public void addTab(TabSpec tabSpec) {
        Log.e("zxw", "addTab: " + tabSpec);
        super.addTab(tabSpec);
    }

    @Override
    public void clearAllTabs() {
        Log.e("zxw", "clearAllTabs: ");
        super.clearAllTabs();
    }

    @Override
    public TabWidget getTabWidget() {
        Log.e("zxw", "getTabWidget: ");
        return super.getTabWidget();
    }

    @Override
    public int getCurrentTab() {
        Log.e("zxw", "getCurrentTab: " + super.getCurrentTab());
        return super.getCurrentTab();
    }

    @Override
    public String getCurrentTabTag() {
        Log.e("zxw", "getCurrentTabTag: " + super.getCurrentTabTag());
        return super.getCurrentTabTag();
    }

    @Override
    public View getCurrentTabView() {
        Log.e("zxw", "getCurrentTabView: " + super.getCurrentTabView());
        return super.getCurrentTabView();
    }

    @Override
    public View getCurrentView() {
        Log.e("zxw", "getCurrentView: " + super.getCurrentView());
        return super.getCurrentView();
    }

    @Override
    public void setCurrentTabByTag(String tag) {
        Log.e("zxw", "setCurrentTabByTag: " + tag);
        super.setCurrentTabByTag(tag);
    }

    @Override
    public FrameLayout getTabContentView() {
        Log.e("zxw", "getTabContentView: ");
        return super.getTabContentView();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("zxw", "dispatchKeyEvent: ");
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        Log.e("zxw", "getAccessibilityClassName: ");
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        Log.e("zxw", "getAccessibilityClassName: ");
        return super.getAccessibilityClassName();
    }

    @Override
    public void setCurrentTab(int index) {
        super.setCurrentTab(index);
        Log.e("zxw", "setCurrentTab: " + index);
    }
}
