package com.example.ilbi;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class CheckReceiver extends BroadcastReceiver {

    private Object TimerNotificationService;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent camera_intent = new Intent(context.getApplicationContext(), CameraActivity.class);
        camera_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(camera_intent);

//        if(intent.getExtras().getInt("requestCode",-1) == 1){
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService((String) TimerNotificationService);
//            notificationManager.cancel(3); // cancel(알림 특정 id)
//        }
    }
}
