package com.android.internal.telephony;

/**
 * Created by louisnard on 12/01/2018.
 */

public interface ITelephony {

    boolean endCall();
    void answerRingingCall();
    void silenceRinger();

}
