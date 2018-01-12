package com.avisto.callfilter;

/**
 * Created by louisnard on 11/01/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class CallBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = CallBroadcastReceiver.class.getSimpleName();

    // Actions
    private final String ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE";

    // Phone call state
    private static int mLastState = TelephonyManager.CALL_STATE_IDLE;


    @Override
    public void onReceive(Context context, Intent intent) {
        final String TAG = this.TAG + ", onReceive()";

        final String action = intent.getAction();

        Log.d(TAG, "action = " + action);

        if (action == ACTION_PHONE_STATE) {
            // Get phone call state and number
            final String stateString = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            final String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.d(TAG, "state = " + stateString);
            Log.d(TAG, "number = " + number);
            int state = 0;
            if (stateString.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                // Phone call hanged up
                state = TelephonyManager.CALL_STATE_IDLE;
                Log.d(TAG, "action = " + action);
            } else if (stateString.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                // Phone call picked up
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateString.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                // Phone call ringing, waiting for decision
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            if (state != mLastState) {
                mLastState = state;
                onCallStateChanged(context, state, number);
            }
        }
    }

    private void onCallStateChanged(Context context, int state, String number) {
        final String TAG = this.TAG + ", onCallStateChanged()";
        Contact contact;
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                // Phone call ringing, waiting for decision
                contact = ContactHelper.getContactByNumber(context, number);
                Log.d(TAG, "CALL_STATE_RINGING with number: " + number);
                if (contact != null) {
                    Log.d(TAG, "Matching incoming call contact: " + contact.getmDisplayName());
                } else {
                    Log.d(TAG, "Unknown incoming call contact");
                }


                break;
        }
    }

    private void rejectIncomingCall(Context context) {
        ITelephony telephonyService;
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            final Class c = Class.forName(telephonyManager.getClass().getName());
            final Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephonyManager);
            telephonyService.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void silenceIncomingCall(Context context) {
        ITelephony telephonyService;
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            final Class c = Class.forName(telephonyManager.getClass().getName());
            final Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephonyManager);
            telephonyService.silenceRinger();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
