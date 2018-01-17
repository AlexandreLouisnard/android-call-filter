package com.louisnard.callfilter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by louisnard on 11/01/2018.
 */

public class UtilsHelper {

    private static String NOTIFICATIONS_CHANNEL_ID = "CALL_FILTER_NOTIFICATIONS_CHANNEL_ID";
    private static int PERMANENT_NOTIFICATION_ID = 456987;

    public static boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void displayPermanentNotification(Context context, boolean display) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (display) {
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATIONS_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.notification_call_filtering_enabled));
            final Intent resultIntent = new Intent(context, MainActivity.class);
            final PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            final Notification notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            notificationManager.notify(PERMANENT_NOTIFICATION_ID, notification);
        } else {
            notificationManager.cancel(PERMANENT_NOTIFICATION_ID);
        }

    }
}
