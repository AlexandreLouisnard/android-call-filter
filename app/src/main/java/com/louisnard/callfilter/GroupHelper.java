package com.louisnard.callfilter;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisnard on 12/01/2018.
 */

public class GroupHelper {

    private static final String TAG = GroupHelper.class.getSimpleName();

    public static List<Group> getAllGroups(Context context) {
        final String TAG2 = TAG + ", getAllGroups()";

        final Cursor groupsCursor = context.getContentResolver().query(
                ContactsContract.Groups.CONTENT_URI,
                new String[]{
                        ContactsContract.Groups._ID,
                        ContactsContract.Groups.TITLE
                }, null, null, ContactsContract.Groups.TITLE
        );

        final List<Group> groups = new ArrayList<>();
        if (groupsCursor != null) {
            while (groupsCursor.moveToNext()) {
                final String id = groupsCursor.getString(groupsCursor.getColumnIndex(ContactsContract.Groups._ID));
                final String title = groupsCursor.getString(groupsCursor.getColumnIndex(ContactsContract.Groups.TITLE));
                groups.add(new Group(id, title));
            }
        }

        Log.d(TAG2, "Found " + groups == null ? "0" : String.valueOf(groups.size()) + " groups");
        return groups;
    }

    public static Group getGroupById(Context context, String groupId) {
        final String TAG2 = TAG + ", getGroupById()";

        final Cursor groupCursor = context.getContentResolver().query(
                ContactsContract.Groups.CONTENT_URI, new String[]{
                        ContactsContract.Groups._ID,
                        ContactsContract.Groups.TITLE
                }, ContactsContract.Groups._ID + "=?", new String[]{groupId}, null
        );

        if (groupCursor != null && groupCursor.getCount() > 0) {
            groupCursor.moveToNext();
            final String title = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.TITLE));
//            Log.d(TAG2, "Found group " + title + " for group id: " + groupId);
            return new Group(groupId, title);
        } else {
//            Log.d(TAG2, "No group found for group id: " + groupId);
            return null;
        }
    }
}
