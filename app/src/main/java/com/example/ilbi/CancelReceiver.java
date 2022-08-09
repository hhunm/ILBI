package com.example.ilbi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CancelReceiver extends BroadcastReceiver {
    static final String TAG = "CancelReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d(TAG,intent.getAction()+"\n");
        TimerNotificationService.stop = true;
//        Log.d(TAG, Boolean.toString(TimerNotificationService.stop));

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference isReport = database.getReference("isReport");
        isReport.setValue("CANCEL");


    }
}
