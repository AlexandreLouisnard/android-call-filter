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
import java.util.HashSet;
import java.util.Set;

public class CallBroadcastReceiver extends BroadcastReceiver {

    // TODO improvement : choose between reject call (goes to voicemail) and silence call (no ring tone)
    // TODO improvement : generate notification when a call has been blocked

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

        if (!SharedPreferencesHelper.isCallFilteringActivated(context)) {
            Log.d(TAG, "Call filtering is NOT activating, returning now");
            return;
        }

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

                if (SharedPreferencesHelper.filterAllContacts(context)) {
                    rejectIncomingCall(context);
                    return;
                }

                if(contact == null) {
                    // Unkwnown contact
                    Log.d(TAG, "Unknown contact incoming call : " + (SharedPreferencesHelper.filterUnkwnownContacts(context) ? "blocking" : "allowing"));
                    if(SharedPreferencesHelper.filterUnkwnownContacts(context)) {
                        rejectIncomingCall(context);
                        return;
                    }
                } else {
                    // Known contact
                    Log.d(TAG, "Matching incoming call contact: " + contact.getmDisplayName());
                    Set<String> intersection = new HashSet<String>(contact.getmGroupIds());
                    intersection.retainAll(SharedPreferencesHelper.getGroupIdsToFilter(context));
                    if (intersection.size() > 0) {
                        // At least one of the contact's groups is filtered
                        Log.d(TAG, "Blocking incoming call because " + contact.getmDisplayName() + " belongs to " + intersection.size() + " blocked group(s)");
                        rejectIncomingCall(context);
                    } else {
                        // This contact's groups are not filtered
                        Log.d(TAG, "Allowing incoming call");
                        return;
                    }
                }
                break;
        }
    }

    private void rejectIncomingCall(Context context) {
        final String TAG = this.TAG + ", rejectIncomingCall()";
        Log.d(TAG, " ");
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
        final String TAG = this.TAG + ", silenceIncomingCall()";
        Log.d(TAG, " ");
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
