package com.avisto.callfilter;

/**
 * Created by louisnard on 12/01/2018.
 */

public class Contact {

    private String mId;
    private String mDisplayName;
    private Group mGroup;

    public Contact(String mId, String mDisplayName, Group mGroup) {
        this.mId = mId;
        this.mDisplayName = mDisplayName;
        this.mGroup = mGroup;
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

    public Group getmGroup() {
        return mGroup;
    }

    public void setmGroup(Group mGroup) {
        this.mGroup = mGroup;
    }
}
