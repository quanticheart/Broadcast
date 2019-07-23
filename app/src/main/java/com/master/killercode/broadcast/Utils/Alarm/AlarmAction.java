package com.master.killercode.broadcast.Utils.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.master.killercode.broadcast.R;

import java.util.concurrent.atomic.AtomicInteger;

public class AlarmAction extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ALARME", "EXECUTADO");

        if (intent.getBooleanExtra("alarmday", false)) {
            NotificationDebug(context, "alarmday");
        }

        if (intent.getBooleanExtra("alarmminute", false)) {
            NotificationDebug(context, "alarmminute");
        }


        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        boolean isScreenOn = pm.isInteractive();

        if (!isScreenOn) {
            NotificationDebug(context, "Screen ON");
        } else {
            NotificationDebug(context, "Screen OFF");
        }
    }

    // ** Notification
    //==============================================================================================

    private final static AtomicInteger c = new AtomicInteger(0);

    public static int getID() {
        return c.incrementAndGet();
    }

    private void NotificationDebug(Context context, String msg) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(msg)
                .setSmallIcon(R.mipmap.ic_launcher).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        assert notificationManager != null;
        notificationManager.notify(getID(), notification);
    }
}
