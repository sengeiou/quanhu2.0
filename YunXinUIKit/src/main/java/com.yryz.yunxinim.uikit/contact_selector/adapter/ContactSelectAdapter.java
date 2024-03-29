package com.yryz.yunxinim.uikit.contact_selector.adapter;

import android.content.Context;

import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.contact.core.item.AbsContactItem;
import com.yryz.yunxinim.uikit.contact.core.item.ContactItem;
import com.yryz.yunxinim.uikit.contact.core.item.ItemTypes;
import com.yryz.yunxinim.uikit.contact.core.model.ContactDataAdapter;
import com.yryz.yunxinim.uikit.contact.core.model.ContactGroupStrategy;
import com.yryz.yunxinim.uikit.contact.core.model.IContact;
import com.yryz.yunxinim.uikit.contact.core.query.IContactDataProvider;
import com.yryz.yunxinim.uikit.contact.core.util.ContactHelper;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ContactSelectAdapter extends ContactDataAdapter {
    private HashSet<String> selects = new HashSet<String>();

    public ContactSelectAdapter(Context context,
                                ContactGroupStrategy groupStrategy, IContactDataProvider dataProvider) {
        super(context, groupStrategy, dataProvider);
    }

    public final void setAlreadySelectedAccounts(List<String> accounts) {
        selects.addAll(accounts);
    }

    public final List<ContactItem> getSelectedItem() {
        if (selects.isEmpty()) {
            return null;
        }

        List<ContactItem> res = new ArrayList<>();
        for (String account : selects) {
            final UserInfoProvider.UserInfo user = NimUIKit.getUserInfoProvider().getUserInfo(account);
            if (user != null) {
                res.add(new ContactItem(ContactHelper.makeContactFromUserInfo(user), ItemTypes.FRIEND));
            }
        }

        return res;
    }

    public final void selectItem(int position) {
        AbsContactItem item = (AbsContactItem) getItem(position);
        if (item != null && item instanceof ContactItem) {
            selects.add(((ContactItem) item).getContact().getContactId());
        }
        notifyDataSetChanged();
    }

    public final boolean isSelected(int position) {
        AbsContactItem item = (AbsContactItem) getItem(position);
        if (item != null && item instanceof ContactItem) {
            return selects.contains(((ContactItem) item).getContact().getContactId());
        }
        return false;
    }

    public final void cancelItem(int position) {
        AbsContactItem item = (AbsContactItem) getItem(position);
        if (item != null && item instanceof ContactItem) {
            selects.remove(((ContactItem) item).getContact().getContactId());
        }
        notifyDataSetChanged();
    }

    public final void cancelItem(IContact iContact) {
        selects.remove(iContact.getContactId());
    }
}
