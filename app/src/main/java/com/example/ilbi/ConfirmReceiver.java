package com.example.ilbi;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmReceiver extends BroadcastReceiver {
    final String TAG = "ConfirmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        TimerNotificationService.delete = true;
        Log.d(TAG,"delete: "+TimerNotificationService.delete);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference isReport = database.getReference("isReport");

        isReport.setValue("INACTIVATED");

    }
}
