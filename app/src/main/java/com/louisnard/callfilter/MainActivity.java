package com.louisnard.callfilter;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;

/**
 * Created by louisnard on 11/01/2018.
 */

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, GroupsAdapter.GroupFilterCheckedChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Required permissions
    private static final String[] requiredPermissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS};

    // Groups
    private List<Group> mGroups;
    private HashSet<String> mGroupIdsToFilter;

    // Views
    private Switch mGlobalSwitch;
    private TextView mStateTextView;
    private Switch mFilterAllContactsSwitch;
    private Switch mFilterUnknownContactsSwitch;
    private TextView mContactGroupsTextView;
    private RecyclerView mRecyclerView;
    private GroupsAdapter mGroupsAdapter;

    // Request codes
    private static final int REQUEST_CODE_PERMISSIONS = 1;

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
        mGroupIdsToFilter = SharedPreferencesHelper.getGroupIdsToFilter(this);

        // Get views
        mGlobalSwitch = findViewById(R.id.switch_global);
        mStateTextView = findViewById(R.id.text_view_state);
        mFilterAllContactsSwitch = findViewById(R.id.switch_filter_all_contacts);
        mFilterUnknownContactsSwitch = findViewById(R.id.switch_filter_unknown_contacts);
        mContactGroupsTextView = findViewById(R.id.text_view_contact_groups);
        mRecyclerView = findViewById(R.id.recycler_view);

        // Set groups to recycler view
        mGroupsAdapter = new GroupsAdapter(this, mGroups, mGroupIdsToFilter, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mGroupsAdapter);

        // Set listeners
        mGlobalSwitch.setOnCheckedChangeListener(this);
        mFilterAllContactsSwitch.setOnCheckedChangeListener(this);
        mFilterUnknownContactsSwitch.setOnCheckedChangeListener(this);

        updateUI();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.switch_global:
                SharedPreferencesHelper.isCallFilteringActivated(this, isChecked);
                updateUI();
                break;
            case R.id.switch_filter_all_contacts:
                SharedPreferencesHelper.filterAllContacts(this, isChecked);
                updateUI();
                break;
            case R.id.switch_filter_unknown_contacts:
                SharedPreferencesHelper.filterUnkwnownContacts(this, isChecked);
                break;
        }
    }

    // Recycler view
    @Override
    public void onGroupFilterCheckedChanged(String groupId, boolean isFiltered) {
        if (isFiltered) {
            mGroupIdsToFilter.add(groupId);
        } else {
            mGroupIdsToFilter.remove(groupId);
        }
        SharedPreferencesHelper.setGroupIdsToFilter(this, mGroupIdsToFilter);
    }

    private void updateUI() {
        final boolean isCallFilteringActivated = SharedPreferencesHelper.isCallFilteringActivated(this);
        final boolean filterAllContacts = SharedPreferencesHelper.filterAllContacts(this);
        final boolean filterUnknownContacts = SharedPreferencesHelper.filterUnkwnownContacts(this);

        mStateTextView.setText(getString(isCallFilteringActivated ? R.string.text_view_call_filtering_enabled : R.string.text_view_call_filtering_disabled));

        mGlobalSwitch.setChecked(isCallFilteringActivated);
        mFilterAllContactsSwitch.setChecked(filterAllContacts);
        mFilterUnknownContactsSwitch.setChecked(filterUnknownContacts);

        mFilterAllContactsSwitch.setVisibility(isCallFilteringActivated ? View.VISIBLE : View.GONE);
        mFilterUnknownContactsSwitch.setVisibility((isCallFilteringActivated && !filterAllContacts) ? View.VISIBLE : View.GONE);
        mContactGroupsTextView.setVisibility((isCallFilteringActivated && !filterAllContacts) ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility((isCallFilteringActivated && !filterAllContacts) ? View.VISIBLE : View.GONE);
    }
}
