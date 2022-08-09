package com.example.ilbi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
    final String TAG = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //만약 어플 최초 구동이면 startView
        SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);

//        SharedPreferences.Editor editor = pref.edit();
//        editor.putBoolean("isFirst", true);
//        editor.commit();

        boolean first = pref.getBoolean("isFirst", true);

        Log.d(TAG, "first: "+ first);

        if(first){
            Log.d(TAG, "first");
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference isCameraOn = database.getReference("isCameraOn");
            isCameraOn.setValue("OFF");

        }
        else{
            Log.d(TAG, "not first");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }
}
