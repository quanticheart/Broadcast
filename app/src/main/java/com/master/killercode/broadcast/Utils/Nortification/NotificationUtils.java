package com.master.killercode.broadcast.Utils.Nortification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.master.killercode.broadcast.MainActivity;
import com.master.killercode.broadcast.R;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Jonh on 27/07/18.
 */
public class NotificationUtils {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";


    public static final String SHARED_PREF = "ah_firebase";

    private static String TAG = NotificationUtils.class.getSimpleName();

    private static Context mContext;

    private static Bitmap bitmap;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        showNotificationMessage(title, message, timeStamp, intent, null);
    }


    // id to handle the notification in the notification tray
    public static final int NORTIFICACAO_NORMAL = 100;
    public static final int NORTIFICACAO_COM_IMAGEM = 101;
    public static final String KEY_NORTIFICACAO_RESPOSTA = "RESPOSTA";

    public static void showNortification(Context context, int value, final String title, final String message) {

        /*
         *         //inicia a intent que sera a ação ao clicar na nortificação
         */


        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra(HMAux.ID_LEILAO , id);
//        intent.putExtra(HMAux.NORTIFICATION , 1);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // inicia a PendingIntent e coloca o valor da intent criada a cima
        final PendingIntent AtividadeResultado =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        /*
         *         //cria a uri do som personalizado para a nortificação
         */
//        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                + "://" + context.getPackageName() + "/raw/judgegavel");

        /*
         *         // Define a PendinigIntent de Resposta
         */
        PendingIntent replyPendingIntent = null;
        // Atividade de chamada em plataformas que não suportam o DirectReply nativamente
        if (Build.VERSION.SDK_INT < 24) {
            replyPendingIntent = AtividadeResultado;
        } else { // Chame o BroadcastReceiver em plataformas que suportam o DirectReply
            replyPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    new Intent(context, MainActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }


        // Criar RemoteInput e anexá-lo à ação de notificação
        final RemoteInput remoteInput = new RemoteInput.Builder(KEY_NORTIFICACAO_RESPOSTA)
                .setLabel("Respondendo a Nortificação")
                .build();

        NotificationCompat.Action AcaoResposta = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_menu_save, "Responder", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();


        long[] v = {500, 1000};

        /*
         *         // Cria a Nortificação
         */

        //cria nortificação corpo personalizada
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.addLine(message);



        //inicia a nortificaçao
        Notification notification = null;
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        NotificationCompat.BigPictureStyle inboxStyle = new NotificationCompat.BigPictureStyle();
        inboxStyle.bigPicture(bitmap);
        inboxStyle.bigLargeIcon(null);

        if (bitmap != null) {
            //icone da nortificação
            final int icon = R.mipmap.ic_launcher;
            notification = mBuilder
                    //header da dortificação
                    .setSmallIcon(icon)//icone do header
                    .setTicker(title)
                    .setWhen(getTimeMilliSec(getTimeNow()))// a hora que foi chamada
                    .setColor(context.getResources().getColor(R.color.colorPrimary))//define a cor do icone e do nome do app na nortificação

                    // mostra que a nortificação pode ser mostradas varias vezes com diferentes conteudos, reutilizando a mesma nortificação
                    .setGroupSummary(true)
                    .setGroup("com.android.example.WORK_EMAIL") // nome do grupo da nortificação

                    //corpo da nortificação
                    .setContentTitle(title)
                    .setContentText(message)
//                  .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))

                    .setLargeIcon(bitmap)

                    //style personalizado da nortifiação
                    .setStyle(inboxStyle)

                    //ação ao clicar na nortificação
                    .setContentIntent(AtividadeResultado)

                    //ação personalizada de resposta na nortificação
//                .addAction(AcaoResposta)

                    //adiciona açoes ao button da dortificação
//                .addAction(android.R.drawable.ic_menu_compass, "Ação 1", AtividadeResultado)
//                .addAction(android.R.drawable.ic_menu_directions, "Ação 2", AtividadeResultado)

                    //mosrtra a nortificação na tela sem ter que abrir a aba superior ( Igual WhatsApp )
                    .setPriority(Notification.PRIORITY_MAX)

                    //som personalizado da nortificação
//                .setSound(alarmSound)

                    //Vibração
                    .setVibrate(v)

                    .setAutoCancel(true)
                    .build();

            if (Build.VERSION.SDK_INT >= 21) mBuilder.setVibrate(new long[1]);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify("teste", value, notification);

        }

    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        // notification icon
        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/zelda_treasure");

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                } else {
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                }
            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
            playNotificationSound();
        }
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NORTIFICACAO_NORMAL, notification);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NORTIFICACAO_COM_IMAGEM, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/zelda_treasure");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void showimgNortification(Context context) {
        mContext = context;
        new RetrieveFeedTask().execute("https://cdn.vox-cdn.com/thumbor/cptb91JTtEWN1JjawU1ftIWj_Lw=/0x0:2040x1360/920x613/filters:focal(860x1034:1186x1360):format(webp)/cdn.vox-cdn.com/uploads/chorus_image/image/59377089/wjoel_180413_1777_android_001.1523625143.jpg");
    }

    static class RetrieveFeedTask extends AsyncTask<String, Void, Bitmap> {

        private Exception exception;

        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Bitmap feed) {

            bitmap = feed;

            showNortification(mContext, 1, "teste", "img");
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }



}
