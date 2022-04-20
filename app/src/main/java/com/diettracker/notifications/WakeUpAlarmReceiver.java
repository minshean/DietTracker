package com.diettracker.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class WakeUpAlarmReceiver extends BroadcastReceiver {

@Override
public void onReceive(Context context, Intent intent) {

    if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();

        calendar1.set(Calendar.HOUR_OF_DAY, 8);
        calendar1.set(Calendar.MINUTE, 30);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        calendar2.set(Calendar.HOUR_OF_DAY, 13);
        calendar2.set(Calendar.MINUTE, 30);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        calendar3.set(Calendar.HOUR_OF_DAY, 19);
        calendar3.set(Calendar.MINUTE, 30);
        calendar3.set(Calendar.SECOND, 0);
        calendar3.set(Calendar.MILLISECOND, 0);


        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar1)) {
            calendar1.add(Calendar.DATE, 1);
        }

        if (cur.after(calendar2)) {
            calendar2.add(Calendar.DATE, 1);
        }

        if (cur.after(calendar3)) {
            calendar3.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(context, DailyReceiver.class);
        int ALARM1_ID = 10000;
        int ALARM2_ID = 20000;
        int ALARM3_ID = 30000;
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                context, ALARM2_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(
                context, ALARM3_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent3);

      }
   }
}