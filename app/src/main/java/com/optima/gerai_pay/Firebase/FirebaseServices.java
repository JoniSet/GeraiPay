package com.optima.gerai_pay.Firebase;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.optima.gerai_pay.Activity.TopupSucsessActivity;
import com.optima.gerai_pay.Helper.SqliteHelper;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FirebaseServices extends FirebaseMessagingService {
    public  static   String title,body;
    private static final String TAG = "MyFirebaseMsgService";
    private SqliteHelper sqliteHelper;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }


    @Override
    public void handleIntent(Intent intent) {
        Log.d( "FCM", "handleIntent ");
        RemoteMessage.Builder remoteMessage     = new RemoteMessage.Builder("FirebaseServices");
        ArrayList<String> list  = new ArrayList<>();
        for (String key : intent.getExtras().keySet())
        {
            Object value = intent.getExtras().get(key);
            Log.d("Sukses", "Key: " + key + " Value: " + value);
            remoteMessage.addData(key, intent.getExtras().get(key).toString());
            list.add(key);
        }

        DateFormat dateFormat       = new SimpleDateFormat("dd MMM yyyy");
        Date date                   = new Date();

        sqliteHelper                = new SqliteHelper(this);

        Log.d("Salah", list.size() + "");
        if (list.size() != 1){
            if (remoteMessage.build().getData().get("tipe").equals("1")) {
                sendNotification(remoteMessage.build().getNotification().getBody(), remoteMessage.build().getNotification().getTitle(), remoteMessage.build().getData().get("tipe"));
            }
            else if (remoteMessage.build().getData().get("tipe").equals("3")) {
                sendNotificationTopup(remoteMessage.build().getNotification().getBody(), remoteMessage.build().getNotification().getTitle(), remoteMessage.build().getData().get("tipe"));
            }
            else if (remoteMessage.build().getData().get("tipe").equals("4")) {
                sqliteHelper.add_notif(
                        remoteMessage.build().getData().get("tipe"),
                        remoteMessage.build().getData().get("title"),
                        remoteMessage.build().getData().get("body"),
                        dateFormat.format(date));
                sendNotificationTrans(remoteMessage.build().getNotification().getBody(), remoteMessage.build().getNotification().getTitle(), remoteMessage.build().getData().get("tipe"));
            }
            else if (remoteMessage.build().getData().get("tipe").equals("5")) {
                sqliteHelper.add_notif(
                        remoteMessage.build().getData().get("tipe"),
                        remoteMessage.build().getData().get("title"),
                        remoteMessage.build().getData().get("body"),
                        dateFormat.format(date));
                sendNotificationTrans(remoteMessage.build().getNotification().getBody(), remoteMessage.build().getNotification().getTitle(), remoteMessage.build().getData().get("tipe"));
            }
            else {
                sendNotificationTrans(remoteMessage.build().getNotification().getBody(), remoteMessage.build().getNotification().getTitle(), remoteMessage.build().getData().get("tipe"));
            }
        }

    }

    @TargetApi(29)
    private void sendNotification(String body, String title, String tipe) {
        if (!isAppIsInBackground(getApplicationContext())) {
            Intent intent = new Intent(this, TopupSucsessActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("body_data", body);
            intent.putExtra("title", title);
            intent.putExtra("tipe", tipe);
            startActivity(intent);

            String channelId = getString(R.string.default_notification_channel_id);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo_geraipay)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    ;

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String canelId      = "User";
                NotificationChannel channel = new NotificationChannel(canelId,
                        "Notification PPOB",
                        NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setSound(null, null);
                Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
                notificationBuilder.setChannelId(canelId);
            }
            Objects.requireNonNull(notificationManager).notify(0, notificationBuilder.build());

        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("body_data", body);
            intent.putExtra("title", title);
            intent.putExtra("tipe", tipe);


            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0 ,
                    intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_notification_channel_id);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo_geraipay)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    ;

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String canelId      = "User";
                NotificationChannel channel = new NotificationChannel(canelId,
                        "Notification PPOB",
                        NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setSound(null, null);
                Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
                notificationBuilder.setChannelId(canelId);
            }
            Objects.requireNonNull(notificationManager).notify(0, notificationBuilder.build());
        }


    }

    @TargetApi(29)
    private void sendNotificationTopup(String body, String title, String tipe) {
        if (!isAppIsInBackground(getApplicationContext())) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("body_data", body);
            intent.putExtra("title", title);
            intent.putExtra("tipe", tipe);
            startActivity(intent);

        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("body_data", body);
            intent.putExtra("title", title);
            intent.putExtra("tipe", tipe);


            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0 ,
                    intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_notification_channel_id);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo_geraipay)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    ;

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String canelId      = "User";
                NotificationChannel channel = new NotificationChannel(canelId,
                        "Notification PPOB",
                        NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setSound(null, null);
                Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
                notificationBuilder.setChannelId(canelId);
            }
            Objects.requireNonNull(notificationManager).notify(0, notificationBuilder.build());
        }


    }

    @TargetApi(29)
    private void sendNotificationTrans(String body, String title, String tipe) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("body_data", body);
        intent.putExtra("title", title);
        intent.putExtra("tipe", tipe);


        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0 ,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo_geraipay)
                .setContentTitle(title)
                .setContentText(body)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
//                        R.drawable.logo_geraipay))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                ;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String canelId      = "User";
            NotificationChannel channel = new NotificationChannel(canelId,
                    "Notification PPOB",
                    NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setSound(null, null);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            notificationBuilder.setChannelId(canelId);
        }
        Objects.requireNonNull(notificationManager).notify(0, notificationBuilder.build());

    }

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

}
