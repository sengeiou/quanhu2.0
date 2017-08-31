package com.yryz.yunxinim.uikit.contact.core.item;

import com.yryz.yunxinim.uikit.contact.core.model.IContact;

import java.util.Collection;

public class ContactTeamIdFilter implements ContactItemFilter {
    private static final long serialVersionUID = -6813849507791265300L;

    private final Collection<String> ids;

    private boolean exclude = true; // false means include

    public ContactTeamIdFilter(Collection<String> ids) {
        this.ids = ids;
    }

    public ContactTeamIdFilter(Collection<String> ids, boolean exclude) {
        this.ids = ids;
        this.exclude = exclude;
    }

    @Override
    public boolean filter(AbsContactItem item) {
        if (item instanceof ContactItem) {
            IContact contact = ((ContactItem) item).getContact();
            boolean contains = !ids.contains(contact.getContactId());
            return exclude ? contains : !contains;
        }

        return false;
    }
}
