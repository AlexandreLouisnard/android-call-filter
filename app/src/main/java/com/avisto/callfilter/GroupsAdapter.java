package com.avisto.callfilter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.HashSet;
import java.util.List;

/**
 * Created by louisnard on 12/01/2018.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private Context mContext;
    private List<Group> mGroups;
    private HashSet<String> mGroupIdsToFilter;
    private GroupFilterCheckedChangedListener mListener;

    // Recycler view switchs listener
    public interface GroupFilterCheckedChangedListener {
        void onGroupFilterCheckedChanged(String groupId, boolean isFiltered);
    }

    public GroupsAdapter(Context context, List<Group> groups, HashSet<String> groupIdsToFilter, GroupFilterCheckedChangedListener listener) {
        mContext = context;
        mGroups = groups;
        mGroupIdsToFilter = groupIdsToFilter;
        mListener = listener;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_group, parent, false);
        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupsAdapter.GroupViewHolder holder, final int position) {
        if(mGroups == null) {
            holder.mFilterGroupSwitch.setText(mContext.getText(R.string.no_groups_found));
            holder.mFilterGroupSwitch.setEnabled(false);
        } else {
            final Group group = mGroups.get(position);
            holder.mFilterGroupSwitch.setText(group.getmTitle());
            holder.mFilterGroupSwitch.setChecked(mGroupIdsToFilter.contains(group.getmId()));
            holder.mFilterGroupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isFiltered) {
                    mListener.onGroupFilterCheckedChanged(group.getmId(), isFiltered);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        public Switch mFilterGroupSwitch;

        public GroupViewHolder(View itemView) {
            super(itemView);
            mFilterGroupSwitch = itemView.findViewById(R.id.switch_filter_contact_group);
        }
    }
}
