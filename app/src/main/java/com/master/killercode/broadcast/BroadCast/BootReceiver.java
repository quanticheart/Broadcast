package com.master.killercode.broadcast.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.master.killercode.broadcast.Utils.Alarm.AlarmManagerUtil;
import com.master.killercode.broadcast.Utils.Alarm.ProjectAlarms;

import static com.master.killercode.broadcast.Utils.Nortification.NotificationUtils.showNortification;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        showNortification(context, 0, "System", "Boot Completed");

        AlarmManagerUtil.checkScreen(context);
        ProjectAlarms.startAllAlarms(context);
//        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//        }

//        <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
//           <receiver
//        android:name=".BroadCast.BootReceiver"
//        android:enabled="true"
//        android:exported="true">
//            <intent-filter>
//                <category android:name="android.intent.category.DEFAULT" />
//
//                <action android:name="android.intent.action.BOOT_COMPLETED" />
//                <action android:name="android.intent.action.QUICKBOOT_POWERON" />
//                <!--For HTC devices-->
//                <action android:name="com.htc.intent.action.QUICKBOOT_POWERON" />
//            </intent-filter>
//        </receiver>
//
//        <receiver android:name=".Utils.Alarm.AlarmeAcao" />

    }
}
