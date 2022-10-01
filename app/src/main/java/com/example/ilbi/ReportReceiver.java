package com.example.ilbi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        SharedPreferences preferences = context.getSharedPreferences("UserInfo", context.MODE_PRIVATE);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference isReport = database.getReference("USER");

        isReport.child(preferences.getString("senior_id","")).child("INFO").child("isReport").setValue("REPORT");

    }
}
