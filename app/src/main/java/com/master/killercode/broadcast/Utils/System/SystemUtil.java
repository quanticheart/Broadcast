package com.master.killercode.broadcast.Utils.System;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.master.killercode.broadcast.MainActivity;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class SystemUtil {

    private Context context;
    private AlertDialog.Builder dialog;

    //Log Nortification
    private static String TAG = "Broadcast Nortifie";
    private static boolean showLog = true;

    public static void log(String msg) {
        if (showLog)
            Log.w(TAG, msg);
    }

    public void logE(String msg) {
        if (showLog)
            Log.e(TAG, msg);
    }

    public static void logAction(String msg) {
        if (showLog)
            Log.d(TAG, msg);
    }

    //control
    public static int LOCATION = 0;
    public static int AIRPLANE_MODE = 1;
    public static int BATTERY = 2;
    public static int WIFI = 3;
    public static int CONNECTION = 4;
    public static int WIFI_STATE = 5;

    public SystemUtil(Context context) {
        this.context = context;
        wifi(null);
        locationEnabled();
    }

    public SystemUtil(Context context, Intent intent, int verifier) {
        this.context = context;

        switch (verifier) {
            case 0:
                locationEnabled();
                break;
            case 1:
                airplaneEnabled(intent);
                break;
            case 2:
                battery(intent);
                break;
            case 3:
                wifi(intent);
                break;
            case 4:
                connection();
                break;
            case 5:
                wifiState(intent);
                break;
        }
    }

    private void wifiState(Intent intent) {
        SupplicantState supState;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        supState = wifiInfo.getSupplicantState();

        String msg = "";

        switch (supState) {
            case COMPLETED:
                msg = "WIFI COMPLETED";
                break;
            case DISCONNECTED:
                msg = "WIFI DISCONNECTED";
                break;
            case DORMANT:
                msg = "DORMANT";
                break;
            case INVALID:
                msg = "INVALID";
                break;
            case INACTIVE:
                msg = "INACTIVE";
                break;
            case SCANNING:
                msg = "SCANNING";
                break;
            case ASSOCIATED:
                msg = "ASSOCIATED";
                break;
            case ASSOCIATING:
                msg = "ASSOCIATING";
                break;
            case UNINITIALIZED:
                msg = "UNINITIALIZED";
                break;
            case AUTHENTICATING:
                msg = "AUTHENTICATING";
                break;
            case GROUP_HANDSHAKE:
                msg = "GROUP_HANDSHAKE";
                break;
            case FOUR_WAY_HANDSHAKE:
                msg = "FOUR_WAY_HANDSHAKE";
                break;
            case INTERFACE_DISABLED:
                msg = "INTERFACE_DISABLED";
                break;
        }

        log(msg);
        MainActivity.setConnectionState(msg);
    }

    private void connection() {

        StringBuilder stringBuilder = new StringBuilder();

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                networkInfo.isConnected()) {
            // Wifi is connected
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();

            log("Wifi connected SSID " + ssid);
            stringBuilder.append("SSID " + ssid);
        }

        //For 3G check
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        //For WiFi Check
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if (networkInfo != null && networkInfo.isConnected()) {
            log("Wifi ok");
            stringBuilder.append(" :Conected");
        } else {
            logE("Internet disabled");
            stringBuilder.append(" :Not Conected");
        }

        if (isWifi) {
            log("Connect isWifi");
            stringBuilder.append(" In wifi");
        } else if (is3g) {

            int networkType = networkInfo.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    log("Connect is2g");
                    stringBuilder.append(" In 2G ");
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    log("Connect is3g");
                    stringBuilder.append(" In 3G ");
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    log("Connect is4g");
                    stringBuilder.append(" In 4G ");
//                default:
//                    log("Connect unknown");
//                    stringBuilder.append(" type unknown");

                    stringBuilder.append(networkInfo.getExtraInfo());
            }


        } else {
            logE("Not connected");
            stringBuilder.append("");
        }

        if (networkInfo != null) {
            NetworkInfo.State state = networkInfo.getState();
            log(networkInfo.toString() + " " + state.toString());
            switch (state) {
                case CONNECTED:
                    break;
                case CONNECTING:
                    break;
                case SUSPENDED:
                    break;
                case DISCONNECTED:
                    break;
                case DISCONNECTING:
                    break;
                case UNKNOWN:
                    break;
            }
        }

        manager = null;

        MainActivity.setWifiInfo(stringBuilder.toString());
    }

    private void wifi(Intent intent) {

        if (intent != null) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    log("Wifi ENABLED");
                    MainActivity.setWifiState(true, "WiFi is ON");
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    log("Wifi ENABLING");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    logE("Wifi Disabled");
                    MainActivity.setWifiState(false, "WiFi is OFF");
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    logE("Wifi DISABLING");
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    logE("Wifi Lossing");
                    break;
            }
        } else {

        }

    }

    private void battery(Intent intent) {

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        log("Battery is changing: " + isCharging);

        if (isCharging) {
//            mTextCarregando.setText("Carregando");
        } else {
//            mTextCarregando.setText("Não Carregando");
        }


        //////////////////////////////////////////////////////////////////////
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (usbCharge) {
//            mTextTipo.setText("Carregando Via USB");
        } else if (acCharge) {
//            mTextTipo.setText("Carregando Via Tomada");
        } else {
//            mTextTipo.setText("Não Carregando");
        }
        log("Battery:  usbCharge: " + usbCharge + " , acCharge: " + acCharge);

        ///////////////////////////////////////////////////////////////////////


        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        log("Battery:  scale: " + scale);
//        mTextViewInfo.setText("Battery Scale : " + scale);

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        log("Battery:  level: " + level);
//        mTextViewInfo.setText(mTextViewInfo.getText() + "\nBattery Level : " + level);

        float percentage = level / (float) scale;
        int mProgressStatus = (int) ((percentage) * 100);
        log("Battery:  ProgressStatus: " + mProgressStatus + "%");

//        mProgressStatus = (int) ((percentage) * 100);
//
//        mTextViewPercentage.setText("" + mProgressStatus + "%");
//
//        mTextViewInfo.setText(mTextViewInfo.getText() + "\nPercentage : " + mProgressStatus + "%");
//
//        mProgressBar.setProgress(mProgressStatus);

    }

    private void airplaneEnabled(Intent intent) {
        // Get all extras
        Bundle extras = intent.getExtras();

        // Fetch the boolean extra using getBoolean()
        boolean state = extras.getBoolean("state");

        // Log the value of the extra
        log("Airplane Mode: " + state);
    }

    //    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    //    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    private void locationEnabled() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            if (lm != null) {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                log("Location: Gps:" + gps_enabled + ", Network:" + network_enabled);
            }
        } catch (Exception ignored) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
//            alertUser();
        }
    }

    private void alertUser() {

        if (dialog == null) {
            dialog = new AlertDialog.Builder(context);
        }

        dialog.setMessage("String here");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
                //get gps
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
            }
        });
        dialog.show();
    }
}
