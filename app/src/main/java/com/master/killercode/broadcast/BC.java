package com.master.killercode.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.master.killercode.broadcast.Utils.System.SystemUtil;

public class BC extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        String action = intent.getAction();
        Log.w("Action", action);

        if (action.equals(Intent.ACTION_POWER_CONNECTED) ||
                action.equals(Intent.ACTION_POWER_DISCONNECTED) ||
                action.equals(Intent.ACTION_BATTERY_CHANGED) ||
                action.equals(Intent.ACTION_BATTERY_LOW) ||
                action.equals(Intent.ACTION_BATTERY_OKAY)) {

            new SystemUtil(context, intent, SystemUtil.BATTERY);
        }

        if (action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
            new SystemUtil(context, intent, SystemUtil.AIRPLANE_MODE);
        }

        if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
            new SystemUtil(context, intent, SystemUtil.LOCATION);
        }

        if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            new SystemUtil(context, intent, SystemUtil.WIFI);
        }

        if (action.equals(Intent.ACTION_SCREEN_ON)){

        }

        if (action.equals(Intent.ACTION_SCREEN_OFF)){

        }

    }
}
