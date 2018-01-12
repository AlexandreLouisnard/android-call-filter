package com.avisto.callfilter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by louisnard on 11/01/2018.
 */

public class ContactHelper {

    private static final String TAG = ContactHelper.class.getSimpleName();

    public static Contact getContactByNumber(Context context, String number) {
        final String TAG2 = TAG + ", getContactByNumber()";
        Log.d(TAG2, "Looking for phone number: " + number);
        final ContentResolver contentResolver = context.getContentResolver();

        Contact contact = null;

        // Find contact by phone number
        final Uri phoneLookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        final Cursor contactLookup = contentResolver.query(phoneLookupUri, new String[]{
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                final String contactId = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.PhoneLookup._ID));
                final String contactName = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

                if (contactId != null && contactName != null) {
                    contact = new Contact(contactId, contactName);
                    Log.d(TAG2, "Found contact name : " + contactName);
                } else {
                    return null;
                }

                // Find contact data
                final Cursor contactDataCursor = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        new String[]{
                                ContactsContract.Data.CONTACT_ID,
                                ContactsContract.Data.DATA1
                        },
                        ContactsContract.Data.MIMETYPE + "=? AND " + ContactsContract.Data.CONTACT_ID + "=?",
                        new String[]{ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE, contactId}, null
                );
                if (contactDataCursor != null) {
                    while (contactDataCursor.moveToNext()) {
                        if(!contactId.equals(contactDataCursor.getString(contactDataCursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)))){
                            // Should never happen
                            return contact;
                        }
                        final String groupId = contactDataCursor.getString(contactDataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                        contact.addGroupId(groupId);
                        final Group group = getGroupById(context, groupId);
                        Log.d(TAG2, "Found contact group : " + group.getmTitle());
                    }
                }
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }
        return contact;
    }

    public static List<Group> getAllGroups(Context context) {
        final Cursor groupsCursor = context.getContentResolver().query(
                ContactsContract.Groups.CONTENT_URI,
                new String[]{
                        ContactsContract.Groups._ID,
                        ContactsContract.Groups.TITLE
                }, null, null, null
        );

        final List<Group> groups = new ArrayList<>();
        if (groupsCursor != null) {
            while (groupsCursor.moveToNext()) {
                final String id = groupsCursor.getString(0);
                final String title = groupsCursor.getString(1);
                groups.add(new Group(id, title));
            }
        }

        return groups;
    }

    public static Group getGroupById(Context context, String groupId) {
        final Cursor groupCursor = context.getContentResolver().query(
                ContactsContract.Groups.CONTENT_URI, new String[]{
                        ContactsContract.Groups._ID,
                        ContactsContract.Groups.TITLE
                }, ContactsContract.Groups._ID + "=?", new String[]{groupId}, null
        );

        if (groupCursor != null && groupCursor.getCount() > 0) {
            groupCursor.moveToNext();
            final String id = groupCursor.getString(0);
            final String title = groupCursor.getString(1);
            return new Group(id, title);
        } else {
            return null;
        }
    }
}
