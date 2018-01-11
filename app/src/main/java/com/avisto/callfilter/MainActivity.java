package com.avisto.callfilter;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by louisnard on 11/01/2018.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Required permissions
    private static final String[] requiredPermissions = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS };

    // Request codes
    private static final int REQUEST_CODE_PERMISSIONS = 1;

    private BroadcastReceiver mBroadcastReceiver = new CallBroadcastReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG = this.TAG + ", onCreate()";

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Request permissions
        if (!UtilsHelper.hasPermissions(this, requiredPermissions)) {
            Log.d(TAG, "Requesting permissions...");
            ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_CODE_PERMISSIONS);
        } else {
            Log.d(TAG, "Permissions already granted");
        }
    }
}
