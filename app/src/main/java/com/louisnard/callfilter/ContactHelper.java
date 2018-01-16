package com.louisnard.callfilter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

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
                    Log.d(TAG2, "Found contact : " + contactId + "/" + contactName);
                } else {
                    return null;
                }

                // Find contact groups
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
                        final String groupId = contactDataCursor.getString(contactDataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                        contact.addGroupId(groupId);
                        final Group group = GroupHelper.getGroupById(context, groupId);
                        Log.d(TAG2, "Found " + contactName + " contact group : " + group.getmId() + "/" + group.getmTitle());
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

}
