package com.example.ilbi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportReceiver extends BroadcastReceiver {
    final String TAG = "ReportReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        TimerNotificationService.report = true;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference isReport = database.getReference("isReport");

        isReport.setValue("REPORT");

    }
}
