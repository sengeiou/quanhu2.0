package com.yryz.yunxinim.uikit.contact.core.provider;

import com.yryz.yunxinim.uikit.contact.core.item.AbsContactItem;
import com.yryz.yunxinim.uikit.contact.core.item.ItemTypes;
import com.yryz.yunxinim.uikit.contact.core.query.IContactDataProvider;
import com.yryz.yunxinim.uikit.contact.core.query.TextQuery;

import java.util.ArrayList;
import java.util.List;

public class ContactDataProvider implements IContactDataProvider {

    private int[] itemTypes;

    public ContactDataProvider(int... itemTypes) {
        this.itemTypes = itemTypes;
    }

    @Override
    public List<AbsContactItem> provide(TextQuery query) {
        List<AbsContactItem> data = new ArrayList<>();

        for (int itemType : itemTypes) {
            data.addAll(provide(itemType, query));
        }

        return data;
    }

    private final List<AbsContactItem> provide(int itemType, TextQuery query) {
        switch (itemType) {
            case ItemTypes.FRIEND:
                return UserDataProvider.provide(query);
            case ItemTypes.TEAM:
            case ItemTypes.TEAMS.ADVANCED_TEAM:
            case ItemTypes.TEAMS.NORMAL_TEAM:
                return TeamDataProvider.provide(query, itemType);
            case ItemTypes.MSG:
                return MsgDataProvider.provide(query);
            default:
                return new ArrayList<>();
        }
    }
}
