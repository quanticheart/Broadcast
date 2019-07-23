package com.master.killercode.broadcast.Utils.Alarm;

import android.content.Context;

public class ProjectAlarms {

    public static void startAllAlarms(Context context) {
        startAlarmByMinutes(context);
        startAlarmEveryDay(context);
    }

    private static void startAlarmEveryDay(Context context) {
        AlarmManagerUtil.startByDay(context, AlarmAction.class, 15);
    }

    private static void startAlarmByMinutes(Context context) {
        AlarmManagerUtil.startByMinutes(context, AlarmAction.class, 1);
    }

}
