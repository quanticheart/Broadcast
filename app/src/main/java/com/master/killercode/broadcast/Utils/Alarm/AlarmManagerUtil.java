package com.master.killercode.broadcast.Utils.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.master.killercode.broadcast.BroadCast.BootReceiver;

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

}
