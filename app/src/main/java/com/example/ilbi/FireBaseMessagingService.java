package com.example.ilbi;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.sql.Time;
import java.util.Map;

public class FireBaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private String title, text;
    private String DEFAULT = "DEFAULT";
    private String TAG = "FireBaseMessagingService";
    private  String remain_min;
    private int remain_ms;
    int progress;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d(TAG, ""+ FirebaseMessaging.getInstance().getToken());

        //title = message.getNotification().getTitle();
        //text = message.getNotification().getBody();
        Map data = message.getData();

        //Log.d(TAG, "title: " +title);
        //Log.d(TAG, "body: " +text);
        Log.d(TAG, ""+ "title: " + data.get("title"));
        Log.d(TAG, ""+ "content: " + data.get("content"));

        //공유 정보
        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String role = preferences.getString("role",null);

        //채널 생성
        if(preferences.getBoolean("DEFAULTCHANNEL",false)) {
            createNotificationChannel(DEFAULT,"default channel");
        }


        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        Intent serviceIntent = new Intent(this, TimerNotificationService.class);
        startForegroundService(serviceIntent);


        //wakeLock.acquire();
        //enqueueWork();
//        if(role.equals("Senior")){
//            //timerNotification(contentIntent);
//            //wakeLock.acquire();
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DEFAULT")
//                    .setPriority(NotificationCompat.PRIORITY_LOW)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("timer notification")
//                    .setContentText("119 신고까지 남은 시간")
//                    .setAutoCancel(true)
//                    .setProgress(60, progress, false);
//
//            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//            Notification noti = builder.build();
//            notificationManager.notify(0, noti);
//            startForeground(0, noti);
//
//            enqueueWork();
//            //wakeLock.release();
//            Log.d(TAG,"senior");
//
//        }else{
//            //일반 push알림
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, DEFAULT)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(title)
//                    .setContentText(text)
//                    .setAutoCancel(true)
//                    //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                    //.setVibrate(new long[]{1,1000})
//                    .setContentIntent(contentIntent);
//
//            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, builder.build());
//            Log.d(TAG,"else");
//        }


//        Intent intent_alert = new Intent(getApplicationContext(), EmergencyActivity.class);
//        intent_alert.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent_alert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent_alert);



    }


    public void createNotificationChannel(String channel_id, String name) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channel_id, name, importance));

        }
    }

}
