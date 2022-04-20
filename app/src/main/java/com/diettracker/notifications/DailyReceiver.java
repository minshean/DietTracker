package com.diettracker.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.diettracker.R;
import com.diettracker.application.MainActivity;

public class DailyReceiver extends BroadcastReceiver {

@Override
public void onReceive(Context context, Intent intent) {

    long when = System.currentTimeMillis();
    NotificationManager notificationManager = (NotificationManager) context
            .getSystemService(Context.NOTIFICATION_SERVICE);

    Intent notificationIntent = new Intent(context, MainActivity.class);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


    NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
            context, context.getString(R.string.default_notification_channel_id)).setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Diet Tracker")
            .setContentText("Don't forget to track your diet and exercises").setSound(alarmSound)
            .setAutoCancel(true).setWhen(when)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
            .setStyle(new NotificationCompat.BigTextStyle().bigText("Don't forget to track your diet and exercises"))
            .setContentIntent(pendingIntent);
    notificationManager.notify(1001, mNotifyBuilder.build());
  }

}