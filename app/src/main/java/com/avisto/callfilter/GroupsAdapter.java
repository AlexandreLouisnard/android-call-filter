package com.avisto.callfilter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import java.util.List;

/**
 * Created by louisnard on 12/01/2018.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private Context mContext;
    private List<Group> mGroups;

    public GroupsAdapter(Context context, List<Group> mGroups) {
        mContext = context;
        this.mGroups = mGroups;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_group, parent, false);
        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupsAdapter.GroupViewHolder holder, int position) {
        if(mGroups == null) {
            holder.mSwitch.setText(mContext.getText(R.string.no_groups_found));
            holder.mSwitch.setEnabled(false);
        } else {
            holder.mSwitch.setText(mGroups.get(position).getmTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        public Switch mSwitch;

        public GroupViewHolder(View itemView) {
            super(itemView);
            mSwitch = itemView.findViewById(R.id.switch_group);
        }
    }
}
