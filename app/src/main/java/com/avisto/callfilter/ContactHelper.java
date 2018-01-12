package com.avisto.callfilter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisnard on 11/01/2018.
 */

public class ContactHelper {

    public static Contact getContactByNumber(Context context, String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        Contact contact = null;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.Groups._ID, ContactsContract.Groups.TITLE }, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
                String name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                String groupId=null;// = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Groups._ID));
                String groupTitle=null;// = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Groups.TITLE));
                contact = new Contact(contactId, name, new Group(groupId, groupTitle));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return contact;
    }

    public static List<Group> getAllGroups(Context context) {
        Cursor groupCursor = context.getContentResolver().query(
                ContactsContract.Groups.CONTENT_URI,
                new String[]{
                        ContactsContract.Groups._ID,
                        ContactsContract.Groups.TITLE
                }, null, null, null
        );

        List<Group> groups = new ArrayList<>();

        if(groupCursor != null){
            while(groupCursor.moveToNext()){
                String id = groupCursor.getString(0);
                String title = groupCursor.getString(1);
                groups.add(new Group(id, title));
            }
        }

        return groups;
    }

    /*
    Cursor groupCursor = getContentResolver().query(
            ContactsContract.Groups.CONTENT_URI,
            new String[]{
                    ContactsContract.Groups._ID,
                    ContactsContract.Groups.TITLE
            }, null, null, null
    );
    */

}
