package com.avisto.callfilter;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.List;

/**
 * Created by louisnard on 11/01/2018.
 */

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Required permissions
    private static final String[] requiredPermissions = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS };

    // Groups
    private List<Group> mGroups;

    // Views
    private Switch mGlobalSwitch;
    private RecyclerView mRecyclerView;
    private GroupsAdapter mGroupsAdapter;

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

        // Get groups
        mGroups = GroupHelper.getAllGroups(this);

        // Get views
        mGlobalSwitch = findViewById(R.id.switch_global);
        mRecyclerView = findViewById(R.id.recycler_view);

        // Set groups to recycler view
        mGroupsAdapter = new GroupsAdapter(this, mGroups);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mGroupsAdapter);

        // Set listeners
        mGlobalSwitch.setOnCheckedChangeListener(this);

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.switch_global:
                SharedPreferencesHelper.setSharedPreferenceBoolean(this, SharedPreferencesHelper.KEY_BOOLEAN_GLOBAL_FILTERING_ACTIVATION, isChecked);
                mRecyclerView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        }
    }
}
