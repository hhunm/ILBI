package com.example.ilbi;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RelativeLayout layout_fall;
    private RelativeLayout layout_camera;
    private RelativeLayout layout_call;
    private RelativeLayout layout_record;
    private RelativeLayout layout_protector;
    private RelativeLayout layout_help;
    private Switch st_camera;
    private final String TAG = "MainActivity";
    public static final int TEXT_SIZE_NORMAL = 22;
    public static final int TEXT_SIZE_BIG = 28;
    private SharedPreferences preferences;

    private User user = User.getInstance();


    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //툴바
        toolbarInit();
        //레이아웃
        layoutInit();


        //sms 권한 확인
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(getApplicationContext(),"SMS수신 권한 있음",Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(getApplicationContext(),"SMS수신 권한 없음",Toast.LENGTH_SHORT).show();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(
                        new String[]{Manifest.permission.SEND_SMS}, 1000);
            }

        }

        //오버레이
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent, 5469);
        }else{
            //Toast.makeText(getApplicationContext(),"오버레이 권한 있음",Toast.LENGTH_SHORT).show();
        }

        //fcm
        FirebaseMessaging.getInstance().subscribeToTopic("fall").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getApplicationContext(),"구독 성공",Toast.LENGTH_SHORT).show();
                    //구독성공뜸
                }else{
                    Toast.makeText(getApplicationContext(),"구독 실패",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //카메라 스위치
        boolean isNormal = preferences.getBoolean("isNormal",true);
        int tSize;
        if(isNormal){
            tSize = TEXT_SIZE_NORMAL;
        }else{
            tSize = TEXT_SIZE_BIG;
        }

//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
//        DatabaseReference isCameraOn = database.getReference("User");
//
//        //데베 리스너
//        // Read from the database
//        isCameraOn.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                SharedPreferences.Editor editor = preferences.edit();
//
//                Log.d(TAG, "Value is: " + value);
//
//                switch(value){
//                    case "ON":
//                        Log.d(TAG, "카메라 ON");
//                        editor.putBoolean("isCameraOn",true);
//                        camera_layout(true, tSize);
//                        break;
//                    case "OFF":
//                        Log.d(TAG, "카메라 OFF");
//                        editor.putBoolean("isCameraOn",false);
//                        camera_layout(false, tSize);
//                        break;
//                }
//                editor.commit();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("USER");
        DatabaseReference myRef;

        if(user.getMy_role()){
            myRef = userRef.child(user.getMy_id()).child("INFO").child("Camera");
        }else{
            myRef = userRef.child(user.getPartner_id()).child("INFO").child("Camera");
        }

        //데베 리스너
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                SharedPreferences.Editor editor = preferences.edit();

                switch(value){
                    case "ON":
                        Log.d(TAG, "카메라 ON");
                        camera_layout(true, tSize);
                        editor.putBoolean("isCameraOn",true);
                        break;
                    case "OFF":
                        Log.d(TAG, "카메라 OFF");
                        camera_layout(false, tSize);
                        editor.putBoolean("isCameraOn",false);
                        break;
                }
                editor.commit();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        st_camera = findViewById(R.id.switch_fall);
        st_camera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myRef.setValue("ON");

                }else{
                    myRef.setValue("OFF");

                }
            }

        });



        //camera 클릭 이벤트
        layout_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"camera",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

        //protector 클릭 이벤트
        layout_protector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"protector",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ProtectorActivity.class);
                startActivity(intent);
            }
        });

        //record 클릭 이벤트
        layout_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"record",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(intent);
            }
        });

        //긴급호출
        layout_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이얼로그
                AlertDialog.Builder callDialog = new AlertDialog.Builder(MainActivity.this);

                callDialog.setMessage("119에 신고하시겠습니까?")
                        .setTitle("119 신고")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this,"예",Toast.LENGTH_SHORT).show();
                        try{
                            SmsManager smsManager = SmsManager.getDefault();
                            if(smsManager == null){
                                return;
                            }
                            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분", Locale.KOREAN);
                            TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
                            format.setTimeZone(tz);
                            Date current_time = new Date();
                            Log.d(TAG, format.format(current_time));
                            //SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                            String msg = format.format(current_time)+" "+user.getSenior_address() + "에서 낙상 사고를 감지했습니다. ";
                            Log.d(TAG, "protector num: " + user.getProtector_number());
                            smsManager.sendTextMessage(user.getProtector_number(),null, msg,null,null);
                            Toast.makeText(MainActivity.this,"전송에 성공했습니다.",Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            Toast.makeText(MainActivity.this,"전송에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }).setNeutralButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this,"아니오",Toast.LENGTH_SHORT).show();
                    }
                }).show();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_mypage:
                Intent intent = new Intent(getApplicationContext(), UserViewActivity.class);
                startActivity(intent);
                return true;

        }
        return true;
    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MainActivity.this.finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void layoutInit(){
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        boolean isNormal = preferences.getBoolean("isNormal",true);

        int tSize;
        if(isNormal){
            tSize = TEXT_SIZE_NORMAL;
        }else{
            tSize = TEXT_SIZE_BIG;
        }

        //기기 해상도 정보 가져오기
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Log.d("main",Integer.toString(metrics.widthPixels));

        //메인 패딩 설정
        View view_main = findViewById(R.id.view_main);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        //fall 뷰 패딩 설정
        layout_fall = findViewById(R.id.layout_fall);

        //fall 이미지 뷰
        ImageView fall_image = findViewById(R.id.img_fall);
        RelativeLayout.LayoutParams fall_img_pr = (RelativeLayout.LayoutParams) fall_image.getLayoutParams();
        fall_image.setImageResource(R.drawable.fall_image);
        fall_img_pr.width = metrics.widthPixels / 6;
        fall_img_pr.height = metrics.widthPixels / 6;
        int margin_inside= metrics.widthPixels / 100 * 2;
        fall_img_pr.setMarginEnd(margin_inside);


        //fall 스위치
        Switch fall_switch = findViewById(R.id.switch_fall);
        if(user.getMy_role()) {
            RelativeLayout.LayoutParams fall_switch_pr = (RelativeLayout.LayoutParams) fall_switch.getLayoutParams();
            fall_switch_pr.width = 320;
            fall_switch_pr.height = 40;
            fall_switch.setText("OFF");
        }else{
            fall_switch.setVisibility(View.GONE);
        }

        //camera on off 확인
//        boolean isCameraOn = preferences.getBoolean("isCameraOn",false);
//        if(isCameraOn){
//            fall_switch.setChecked(true);
//        }else{
//            fall_switch.setChecked(false);
//        }
//        camera_layout(isCameraOn, tSize);

        //camera 뷰 패딩 설정
        layout_camera = findViewById(R.id.view_camera);

        //camera 이미지 뷰
        ImageView camera_image = findViewById(R.id.img_camera);
        RelativeLayout.LayoutParams camera_img_pr = (RelativeLayout.LayoutParams) camera_image.getLayoutParams();
        camera_image.setImageResource(R.drawable.camera_image);
        camera_img_pr.width = metrics.widthPixels / 6;
        camera_img_pr.height = metrics.widthPixels / 6;
        camera_img_pr.setMarginEnd(margin_inside);

        //camera 타이틀 텍스트 뷰
        TextView camera_title = findViewById(R.id.txt_camera_title);
        camera_title.setText("실시간 모니터링");
        camera_title.setTextSize(Dimension.SP, tSize);

        //camera 내용 텍스트 뷰
        TextView camera_content = findViewById(R.id.txt_camera_content);
        camera_content.setText("카메라의 영상을 실시간으로 확인할 수 있습니다");
        camera_content.setTextSize(Dimension.SP, tSize);

        //call 뷰 패딩 설정
        layout_call = findViewById(R.id.layout_call);

        //call 이미지 뷰
        ImageView call_image = findViewById(R.id.img_call);
        RelativeLayout.LayoutParams call_img_pr = (RelativeLayout.LayoutParams) call_image.getLayoutParams();
        call_image.setImageResource(R.drawable.call_image);
        call_img_pr.width = metrics.widthPixels / 7;
        call_img_pr.height = metrics.widthPixels / 7;
        call_img_pr.setMarginEnd(margin_inside);

        //call 타이틀 텍스트 뷰
        TextView call_title = findViewById(R.id.txt_call_title);
        call_title.setText("긴급호출");
        call_title.setTextSize(Dimension.SP, tSize);

        //call 내용 텍스트 뷰
        TextView call_content = findViewById(R.id.txt_call_content);
        call_content.setText("119에 긴급신고 문자를 보냅니다");
        call_content.setTextSize(Dimension.SP, tSize);


        //protector 뷰 패딩 설정
        layout_protector = findViewById(R.id.layout_protector);


        //protector 이미지 뷰
        ImageView protector_image = findViewById(R.id.img_protector);
        RelativeLayout.LayoutParams protector_img_pr = (RelativeLayout.LayoutParams) protector_image.getLayoutParams();
        protector_image.setImageResource(R.drawable.protector_icon_image);
        protector_img_pr.width = metrics.widthPixels / 7;
        protector_img_pr.height = metrics.widthPixels / 7;
        protector_img_pr.setMarginEnd(margin_inside);

        //protector 타이틀 텍스트 뷰
        TextView protector_title = findViewById(R.id.txt_protector_title);
        protector_title.setText("보호자");
        protector_title.setTextSize(Dimension.SP, tSize);

        //protector 보호자 이름 텍스트 뷰
        TextView protector_name = findViewById(R.id.txt_protector_name);
        protector_name.setText("보호자 정보를 확인할 수 있습니다");
        protector_name.setTextSize(Dimension.SP, tSize);

        //protector 번호 텍스트 뷰
        TextView protector_number = findViewById(R.id.txt_protector_number);
        protector_number.setText("");

        //record 뷰
        layout_record = findViewById(R.id.layout_record);

        ImageView record_image = findViewById(R.id.img_record);
        RelativeLayout.LayoutParams record_img_pr = (RelativeLayout.LayoutParams) record_image.getLayoutParams();
        record_image.setImageResource(R.drawable.record);
        record_img_pr.width = metrics.widthPixels / 7;
        record_img_pr.height = metrics.widthPixels / 7;
        record_img_pr.setMarginEnd(30);

        TextView record_title = findViewById(R.id.txt_record_title);
        record_title.setText("낙상기록");
        record_title.setTextSize(Dimension.SP, tSize);


        TextView record1 = findViewById(R.id.txt_record_content);
        record1.setText("낙상기록을 확인할 수 있습니다");
        record1.setTextSize(Dimension.SP, tSize);


//        //help 뷰 패딩 설정
//        layout_help = findViewById(R.id.layout_help);
//
//        //help 이미지 뷰
//        ImageView help_image = findViewById(R.id.img_help);
//        RelativeLayout.LayoutParams help_img_pr = (RelativeLayout.LayoutParams) help_image.getLayoutParams();
//        help_image.setImageResource(R.drawable.help_image);
//        help_img_pr.width = metrics.widthPixels / 7;
//        help_img_pr.height = metrics.widthPixels / 7;
//        help_img_pr.setMarginEnd(margin_inside);
//
//        //help 타이틀 텍스트 뷰
//        TextView help_title = findViewById(R.id.txt_help_title);
//        help_title.setText("도움말");
//        help_title.setTextSize(Dimension.SP, tSize);


    }
    void camera_layout(boolean isON,int tSize){
        st_camera = findViewById(R.id.switch_fall);
        //fall 타이틀 텍스트 뷰
        TextView fall_title = findViewById(R.id.txt_fall_title);
        //fall 내용 텍스트 뷰
        TextView fall_content = findViewById(R.id.txt_fall_content);
        if(isON){
            fall_content.setText("카메라가 켜져 있습니다");
            fall_content.setTextSize(Dimension.SP, tSize);
            fall_title.setText("낙상 감지 중 입니다");
            fall_title.setTextSize(Dimension.SP, tSize);
            st_camera.setText("ON");
            st_camera.setChecked(true);
        }else{
            fall_content.setText("카메라가 꺼져 있습니다");
            fall_content.setTextSize(Dimension.SP, tSize);
            fall_title.setText("낙상 감지가 중단되었습니다");
            fall_title.setTextSize(Dimension.SP, tSize);
            st_camera.setText("OFF");
            st_camera.setChecked(false);
        }
    }
}


