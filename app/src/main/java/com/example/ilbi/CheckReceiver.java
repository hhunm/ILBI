package com.example.ilbi;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class CheckReceiver extends BroadcastReceiver {

    private Object TimerNotificationService;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        User user = User.getInstance();
        SharedPreferences preferences = context.getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        user.setMy_id(preferences.getString("my_id",""));
        user.userInit(context.getApplicationContext(), new Intent(context.getApplicationContext(), CameraActivity.class));

//        if(intent.getExtras().getInt("requestCode",-1) == 1){
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService((String) TimerNotificationService);
//            notificationManager.cancel(3); // cancel(알림 특정 id)
//        }
    }
}
