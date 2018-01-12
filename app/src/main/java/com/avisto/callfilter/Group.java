package com.avisto.callfilter;

/**
 * Created by louisnard on 12/01/2018.
 */

public class Group {
    private String mId;
    private String mTitle;

    public Group(String mId, String mTitle) {
        this.mId = mId;
        this.mTitle = mTitle;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
