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

/**
 * Created by nalmir on 05/12/2017.
 */

public class AlarmAction extends BroadcastReceiver {

    private final static AtomicInteger c = new AtomicInteger(0);
    
    public static int getID() {
        return c.incrementAndGet();
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
//
//        Toast.makeText(
//                context,
//                "É nóis na fita!!!",
//                Toast.LENGTH_SHORT
//        ).show();
//        //
        Log.d("ALARME", "EXECUTADO");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive();

        if (!isScreenOn) {
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = new Notification.Builder(context)
                    .setContentTitle(
                            context.getResources().getString(R.string.app_name))
                    .setContentText("Screen OFF")
                    .setSmallIcon(R.mipmap.ic_launcher).build();

            // hide the notification after its selected
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(getID(), notification);
        }
    }
    
}
