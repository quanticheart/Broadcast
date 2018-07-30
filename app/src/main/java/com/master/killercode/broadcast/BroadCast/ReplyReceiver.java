package com.master.killercode.broadcast.BroadCast;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ReplyReceiver extends BroadcastReceiver {
    public ReplyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {

            CharSequence id = remoteInput.getCharSequence("");

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Resposta da nortificação :" + id);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(10, mBuilder.build());
        }
    }
}
