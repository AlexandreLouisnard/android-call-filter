package com.avisto.callfilter;

import java.util.HashSet;

/**
 * Created by louisnard on 12/01/2018.
 */

public class Contact {

    private String mId;
    private String mDisplayName;
    private HashSet<String> mGroupIds;

    public Contact(String mId, String mDisplayName) {
        this.mId = mId;
        this.mDisplayName = mDisplayName;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public HashSet<String> getmGroupIds() {
        return mGroupIds;
    }

    public void setmGroupIds(HashSet<String> mGroupIds) {
        this.mGroupIds = mGroupIds;
    }

    public void addGroupId(String groupId) {
        if (mGroupIds == null) {
            mGroupIds = new HashSet<>();
        }
        mGroupIds.add(groupId);
    }
}
