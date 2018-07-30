package com.master.killercode.broadcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.master.killercode.broadcast.Utils.Alarm.AlarmManagerUtil;
import com.master.killercode.broadcast.Utils.Nortification.NotificationUtils;
import com.master.killercode.broadcast.Utils.System.SystemUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private Activity activity;
    private Context context;
    private static WifiManager wifiManager;

    //Broadcast
    private IntentFilter filter;
    private MyReceiver myReceiver;

    //TextView
    private static TextView labelWifi;
    private static TextView labelWifiInfo;
    private static TextView labelConnectionState;

    //Button
    private static Button showNortification;
    private static Button startAlarm;

    //Switch
    private static Switch swWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;
        context = getApplicationContext();

        initVars();
        initActions();
        initBroadcast();

    }

    private void initVars() {
        //TextView
        labelWifi = findViewById(R.id.labelWifi);
        labelWifiInfo = findViewById(R.id.labelWifiInfo);
        labelConnectionState = findViewById(R.id.labelConnectionState);

        //Button
        showNortification = findViewById(R.id.showNortification);
        startAlarm = findViewById(R.id.startAlarm);

        //Switch
        swWifi = findViewById(R.id.swWifi);
    }

    private int value = 0;

    @SuppressLint("WifiManagerPotentialLeak")
    private void initActions() {
        final Notification.InboxStyle inboxStyle = new Notification.InboxStyle();

        startAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManagerUtil.checkScreen(MainActivity.this);
            }
        });

        showNortification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationUtils.showimgNortification(MainActivity.this);
                    value++;


//                for (int i = 0; i < 5; i++) {
//                    NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                    Notification.Builder builder = new Notification.Builder(MainActivity.this);
//                    builder.setContentTitle("Lanes");
//                    builder.setContentText("Notification from Lanes" + value);
//                    builder.setSmallIcon(R.mipmap.ic_launcher);
////                    builder.setLargeIcon(R.mipmap.ic_launcher);
//                    builder.setAutoCancel(true);
//                    inboxStyle.setBigContentTitle("Enter Content Text");
//                    inboxStyle.addLine("hi events " + value);
//                    builder.setStyle(inboxStyle);
//                    nManager.notify("App Name", value, builder.build());
//                }

            }

        });

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        swWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setWifiState(true, "WiFi is ON");
                } else {
                    setWifiState(false, "WiFi is OFF");
                }
            }
        });
        if (wifiManager.isWifiEnabled()) {
            setWifiState(true, "WiFi is ON");
        } else {
            setWifiState(false, "WiFi is OFF");
        }

    }

    public static void setWifiState(Boolean state, String msg) {
        wifiManager.setWifiEnabled(state);
        swWifi.setChecked(state);
        labelWifi.setText(msg);
    }

    public static void setWifiInfo(String msg) {
        labelWifiInfo.setText(msg);
    }

    public static void setConnectionState(String msg) {
        labelConnectionState.setText(msg);
    }

    private void initBroadcast() {

        new SystemUtil(MainActivity.this);
        filter = new IntentFilter();
//        primaryFilter();
        testFilter();

        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, filter);

    }

    private void primaryFilter() {
        //batterychanged
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        //
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        //
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        //
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        //
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        //
        filter.addAction(Intent.ACTION_DEVICE_STORAGE_LOW);
        //
        filter.addAction(Intent.ACTION_DEVICE_STORAGE_OK);
        //
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        //
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

        //conection
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        //
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);  // pega a ação da bateria
        //
        filter.addAction(Intent.ACTION_SCREEN_ON); // pega a ação de ligar a tela
        filter.addAction(Intent.ACTION_SCREEN_OFF); // pega a ação de desligar a tela
        //
        filter.addAction(Intent.ACTION_USER_PRESENT); // pega a ação de usuario presente no aplicativo
    }

    private void testFilter() {
        filter.addAction("android.app.action.ACTION_PASSWORD_CHANGED");
        filter.addAction("android.app.action.ACTION_PASSWORD_EXPIRING");
        filter.addAction("android.app.action.ACTION_PASSWORD_FAILED");
        filter.addAction("android.app.action.ACTION_PASSWORD_SUCCEEDED");
        filter.addAction("android.app.action.DEVICE_ADMIN_DISABLED");
        filter.addAction("android.app.action.DEVICE_ADMIN_DISABLE_REQUESTED");
        filter.addAction("android.app.action.DEVICE_ADMIN_ENABLED");
        filter.addAction("android.app.action.LOCK_TASK_ENTERING");
        filter.addAction("android.app.action.LOCK_TASK_EXITING");
        filter.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        filter.addAction("android.app.action.PROFILE_PROVISIONING_COMPLETE");
        filter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        filter.addAction("android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
        filter.addAction("android.bluetooth.adapter.action.LOCAL_NAME_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.SCAN_MODE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        filter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        filter.addAction("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED");
        filter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        filter.addAction("android.bluetooth.device.action.CLASS_CHANGED");
        filter.addAction("android.bluetooth.device.action.FOUND");
        filter.addAction("android.bluetooth.device.action.NAME_CHANGED");
        filter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
        filter.addAction("android.bluetooth.device.action.UUID");
        filter.addAction("android.bluetooth.devicepicker.action.DEVICE_SELECTED");
        filter.addAction("android.bluetooth.devicepicker.action.LAUNCH");
        filter.addAction("android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT");
        filter.addAction("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED");
        filter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        filter.addAction("android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED");
        filter.addAction("android.bluetooth.pan.profile.action.CONNECTION_STATE_CHANGED");
        filter.addAction("android.hardware.action.NEW_PICTURE");
        filter.addAction("android.hardware.action.NEW_VIDEO");
        filter.addAction("android.hardware.hdmi.action.OSD_MESSAGE");
        filter.addAction("android.hardware.input.action.QUERY_KEYBOARD_LAYOUTS");
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        filter.addAction("android.intent.action.AIRPLANE_MODE");
        filter.addAction("android.intent.action.APPLICATION_RESTRICTIONS_CHANGED");
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        filter.addAction("android.intent.action.BATTERY_LOW");
        filter.addAction("android.intent.action.BATTERY_OKAY");
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        filter.addAction("android.intent.action.CAMERA_BUTTON");
        filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        filter.addAction("android.intent.action.CONTENT_CHANGED");
        filter.addAction("android.intent.action.DATA_SMS_RECEIVED");
        filter.addAction("android.intent.action.DATE_CHANGED");
        filter.addAction("android.intent.action.DEVICE_STORAGE_LOW");
        filter.addAction("android.intent.action.DEVICE_STORAGE_OK");
        filter.addAction("android.intent.action.DOCK_EVENT");
        filter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
        filter.addAction("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED");
        filter.addAction("android.intent.action.DREAMING_STARTED");
        filter.addAction("android.intent.action.DREAMING_STOPPED");
        filter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
        filter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
        filter.addAction("android.intent.action.FETCH_VOICEMAIL");
        filter.addAction("android.intent.action.GTALK_CONNECTED");
        filter.addAction("android.intent.action.GTALK_DISCONNECTED");
        filter.addAction("android.intent.action.HEADSET_PLUG");
        filter.addAction("android.intent.action.HEADSET_PLUG");
        filter.addAction("android.intent.action.INPUT_METHOD_CHANGED");
        filter.addAction("android.intent.action.LOCALE_CHANGED");
        filter.addAction("android.intent.action.MANAGE_PACKAGE_STORAGE");
        filter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        filter.addAction("android.intent.action.MEDIA_BUTTON");
        filter.addAction("android.intent.action.MEDIA_CHECKING");
        filter.addAction("android.intent.action.MEDIA_EJECT");
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addAction("android.intent.action.MEDIA_NOFS");
        filter.addAction("android.intent.action.MEDIA_REMOVED");
        filter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
        filter.addAction("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        filter.addAction("android.intent.action.MEDIA_SCANNER_STARTED");
        filter.addAction("android.intent.action.MEDIA_SHARED");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addAction("android.intent.action.MY_PACKAGE_REPLACED");
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        filter.addAction("android.intent.action.NEW_VOICEMAIL");
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_CHANGED");
        filter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        filter.addAction("android.intent.action.PACKAGE_FIRST_LAUNCH");
        filter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_INSTALL");
        filter.addAction("android.intent.action.PACKAGE_NEEDS_VERIFICATION");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addAction("android.intent.action.PACKAGE_RESTARTED");
        filter.addAction("android.intent.action.PACKAGE_VERIFIED");
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.PROVIDER_CHANGED");
        filter.addAction("android.intent.action.PROXY_CHANGE");
        filter.addAction("android.intent.action.REBOOT");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.TIME_TICK");
        filter.addAction("android.intent.action.UID_REMOVED");
        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction("android.intent.action.WALLPAPER_CHANGED");
        filter.addAction("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
        filter.addAction("android.media.AUDIO_BECOMING_NOISY");
        filter.addAction("android.media.RINGER_MODE_CHANGED");
        filter.addAction("android.media.SCO_AUDIO_STATE_CHANGED");
        filter.addAction("android.media.VIBRATE_SETTING_CHANGED");
        filter.addAction("android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION");
        filter.addAction("android.media.action.HDMI_AUDIO_PLUG");
        filter.addAction("android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION");
        filter.addAction("android.net.conn.BACKGROUND_DATA_SETTING_CHANGED");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.nsd.STATE_CHANGED");
        filter.addAction("android.net.scoring.SCORER_CHANGED");
        filter.addAction("android.net.scoring.SCORE_NETWORKS");
        filter.addAction("android.net.wifi.NETWORK_IDS_CHANGED");
        filter.addAction("android.net.wifi.RSSI_CHANGED");
        filter.addAction("android.net.wifi.SCAN_RESULTS");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        filter.addAction("android.net.wifi.p2p.DISCOVERY_STATE_CHANGE");
        filter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        filter.addAction("android.net.wifi.p2p.STATE_CHANGED");
        filter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        filter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
        filter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        filter.addAction("android.nfc.action.ADAPTER_STATE_CHANGED");
        filter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        filter.addAction("android.provider.Telephony.SIM_FULL");
        filter.addAction("android.provider.Telephony.SMS_CB_RECEIVED");
        filter.addAction("android.provider.Telephony.SMS_DELIVER");
        filter.addAction("android.provider.Telephony.SMS_EMERGENCY_CB_RECEIVED");
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.provider.Telephony.SMS_REJECTED");
        filter.addAction("android.provider.Telephony.SMS_SERVICE_CATEGORY_PROGRAM_DATA_RECEIVED");
        filter.addAction("android.provider.Telephony.WAP_PUSH_DELIVER");
        filter.addAction("android.provider.Telephony.WAP_PUSH_RECEIVED");
        filter.addAction("android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED");
        filter.addAction("android.speech.tts.engine.TTS_DATA_INSTALLED");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        registerReceiver(myReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }

    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class<?> conmanClass = Class.forName(conman.getClass().getName());
        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
        final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);
        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
    }

}

