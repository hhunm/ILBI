package com.example.ilbi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerNotificationService extends JobIntentService {
    private static final String TAG = "TimerNotification";
    private static final int TIME = 10;

    private int progress;
    static void enqueueWork(Context context, Intent work){
        enqueueWork(context, TimerNotificationService.class,123, work);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel("NOTIFICATION_TIMER", "timer_channel", importance));
        }

        Intent Mintent = new Intent(this, MainActivity.class);
        Mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, Mintent,PendingIntent.FLAG_CANCEL_CURRENT);

        progress = 0;

//        Intent Mintent = new Intent(this, MainActivity.class);
//        Mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent contentIntent = PendingIntent.getActivity(
//                this, 0, Mintent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "NOTIFICATION_TIMER")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("start")
                .setProgress(60, progress, false)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int id = 3;
        Notification noti2 = builder2.build();
        notificationManager.notify(id, noti2);
        startForeground(id, noti2);


        while(true) {
            if (progress == TIME) {
                builder2.setProgress(0, 0, false);
                builder2.setContentTitle("신고 완료");
                notificationManager.notify(id, builder2.build());
                //startForeground(id, noti2);

                Log.d(TAG,"done");
                break;
            } else {
                progress += 1;
                builder2.setContentText(Integer.toString(progress));
                builder2.setProgress(TIME, progress, false);
                builder2.setContentTitle("CountDown");
                notificationManager.notify(id, builder2.build());
                //startForeground(id, noti2);

                Log.d(TAG,Integer.toString(progress));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        }
        Log.d(TAG, "while 탈출");
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork");

    }

    @Override
    public void onDestroy() {
        //stopSelf();
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG,"onStopCurrentWork");
        return super.onStopCurrentWork();

    }
}
