package com.master.killercode.broadcast.Utils.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.master.killercode.broadcast.BroadCast.BootReceiver;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmManagerUtil {

    private static PendingIntent pi;
    private static AlarmManager am;

    public static void checkScreen(Context context) {
        Intent mIntent = new Intent(context, AlarmeAcao.class);
        am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        pi = PendingIntent.getBroadcast(
                context,
                0,
                mIntent,
                0
        );

        am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + (5 * 1000),
                (5 * 1000),
                pi
        );
    }

    public static void startByMinutes(Context context, Class alarmBroadcast, int minutesToStart) {
        Intent mIntent = new Intent(context, alarmBroadcast);
        mIntent.putExtra("alarmminute", true);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                0,
                mIntent,
                0
        );

        if (am != null)
            am.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + (minutesToStart * 1000),
                    (minutesToStart * 1000),
                    pi
            );
    }

    public static void startByDay(Context context, Class alarmBroadcast, int hourToStart) {
        Intent mIntent = new Intent(context, alarmBroadcast);
        mIntent.putExtra("alarmday", true);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hourToStart); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                0,
                mIntent,
                0
        );

        if (am != null)
            am.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pi
            );
    }

    public static void startByDay(Context context, Class alarmBroadcast, Calendar calendar) {
        Intent mIntent = new Intent(context, alarmBroadcast);
        mIntent.putExtra("alarmday", true);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                0,
                mIntent,
                0
        );

        if (am != null)
            am.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pi
            );
    }

}
