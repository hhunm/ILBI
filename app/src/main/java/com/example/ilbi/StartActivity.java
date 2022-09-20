package com.example.ilbi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    Button start_btn;
    Button login_btn;
    private final String TAG = "StartActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        layout_init();

//        try{
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        //만약 어플 최초 구동이면 startView
//        SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
//
////        SharedPreferences.Editor editor = pref.edit();
////        editor.putBoolean("isFirst", true);
////        editor.commit();
//
//        boolean first = pref.getBoolean("isFirst", true);
//
//       Log.d(TAG, "first: "+ first);
//
//        if(first){
//            Log.d(TAG, "first");
//        }
//        else{
//            Log.d(TAG, "not first");
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }

        //버튼 클릭
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StartActivity.this,"start!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), StartGeneralActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(StartActivity.this,"start!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });



    }

    void layout_init(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        ImageView logo = findViewById(R.id.img_logo);
        LinearLayout.LayoutParams logo_pr = (LinearLayout.LayoutParams) logo.getLayoutParams();
        logo_pr.setMargins(0,metrics.heightPixels/4,0,metrics.heightPixels/6);

        login_btn = findViewById(R.id.btn_login);
        LinearLayout.LayoutParams login_btn_pr = (LinearLayout.LayoutParams) login_btn.getLayoutParams();
        login_btn.setText("로그인");
        login_btn_pr.width = metrics.widthPixels/5 * 4;

        start_btn = findViewById(R.id.btn_start);
        LinearLayout.LayoutParams start_btn_pr = (LinearLayout.LayoutParams) start_btn.getLayoutParams();
        start_btn.setText("회원가입");
        start_btn_pr.width = metrics.widthPixels/5 * 4;

    }
}
