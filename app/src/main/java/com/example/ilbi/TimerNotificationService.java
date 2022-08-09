package com.example.ilbi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class TimerNotificationService extends JobIntentService {
    private static final String TAG = "TimerNotification";
    private static final int TIME = 10;
    public static boolean stop = false;
    public static boolean report = false;
    public static boolean delete = false;
    public static String byWho = "";
    private FirebaseDatabase database;
    private int progress;

    static void enqueueWork(Context context, Intent work){
        enqueueWork(context, TimerNotificationService.class,123, work);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        stop = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel("NOTIFICATION_TIMER", "timer_channel", importance));
        }

        Intent Mintent = new Intent(this, MainActivity.class);
        Mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, Mintent,PendingIntent.FLAG_CANCEL_CURRENT);

        //초기화
        progress = 0;

//        Intent Mintent = new Intent(this, MainActivity.class);
//        Mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent contentIntent = PendingIntent.getActivity(
//                this, 0, Mintent,PendingIntent.FLAG_CANCEL_CURRENT);

        //바로신고랑 취소랑 같은 브로드캐스트 리시버를 사용하면서 바로신고이면 requestCode를 1로 줘서 리시버에서 신고
        Intent cancelIntent = new Intent(this, CancelReceiver.class);
        cancelIntent.setAction("cancel");
        cancelIntent.putExtra("requestCode",0);
        PendingIntent cancelPending =
                PendingIntent.getBroadcast(this,0,cancelIntent,0);

        Intent reportIntent = new Intent(this, ReportReceiver.class);
        reportIntent.putExtra("requestCode",1);
        PendingIntent reportPending =
                PendingIntent.getBroadcast(this,1,reportIntent,0);

        Intent checkIntent = new Intent(this, CheckReceiver.class);
        checkIntent.putExtra("requestCode",0);
        PendingIntent checkPending =
                PendingIntent.getBroadcast(this, 0, checkIntent,0);

        //보호자인지 시니어인지 확인
        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String role = preferences.getString("role",null);
        Log.d(TAG,"role: "+ role);

        byWho = preferences.getString("role","");

        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "NOTIFICATION_TIMER")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.mipmap.icon_f)
                .setContentTitle("start")
                .setProgress(TIME, progress, false)
                .setAutoCancel(true)
                .addAction(R.drawable.logo,"취소",cancelPending)
                //.addAction(R.drawable.adaptive_icon,"카메라 확인",checkPending)
                .addAction(R.drawable.logo,"바로 신고",reportPending)
                .setContentIntent(contentIntent);

        if(role.equals("Protector")){
            builder2.addAction(R.drawable.logo,"카메라 확인",checkPending);
        }

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int id = 3;
        Notification noti2 = builder2.build();
        notificationManager.notify(id, noti2);
        startForeground(id, noti2);

        //2022-08-02
        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference isReport = database.getReference("isReport");

        isReport.setValue("ACTIVATED");

        // Read from the database
        isReport.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);

                switch(value){
                    case "ACTIVATED":
                        Log.d("Firebase", "신고 접수");
                        break;
                    case "REPORT":
                        if(!report){
                            TimerNotificationService.report = true;
                            byWho = switchRole(byWho);
                        }
                        Log.d("Firebase", "신고 완료");
                        break;
                    case "CANCEL":
                        if(!stop){
                            TimerNotificationService.stop = true;
                            byWho = switchRole(byWho);
                        }
                        Log.d("Firebase", "신고 취소");
                        break;
                    case "INACTIVATED":
                        Log.d("Firebase", "비활성화");
                        break;

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



//
         Thread timer = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG","Thread");
                progress = 0;
                stop = false;
                report = false;
                delete = false;


                try{
                    while(true) {
                        Log.d(TAG, Boolean.toString(stop));

                        if(stop==true && report==false){
                            //2022.08.23 데베에 기록 추가
                            if(byWho.equals(role)) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 k시 mm분", Locale.KOREAN);
                                TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
                                format.setTimeZone(tz);
                                Date current_time = new Date();

                                DatabaseReference reportRef = database.getReference("RECORD");
                                String key = reportRef.push().getKey();
                                Record rc = new Record(format.format(current_time), false, byWho);
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/record/" + key, rc.recordToMap());
                                reportRef.updateChildren(childUpdates);
                            }
                            stopForeground(true);
                            break;
                        }

                        if (report == true || progress == TIME) {
    //                        Log.d(TAG,"progress: "+Integer.toString(progress)+" TIME: "+TIME);
                            if(progress == TIME){
                                byWho = "TIMEOUT";
                            }
                            Intent confirmIntent = new Intent(getApplicationContext(), ConfirmReceiver.class);
                            confirmIntent.putExtra("requestCode",1);
                            PendingIntent confirmPending =
                                    PendingIntent.getBroadcast(getApplicationContext(), 0, confirmIntent,0);

                            NotificationCompat.Builder end_builder = new NotificationCompat.Builder(getApplicationContext(), "NOTIFICATION_TIMER");
                            end_builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setSmallIcon(R.mipmap.icon_f)
                                    .setContentTitle("신고 완료")
                                    .setContentText("신고가 완료되었습니다")
                                    .setAutoCancel(true)
                                    .addAction(R.drawable.logo,"확인",confirmPending)
                                    .setContentIntent(contentIntent);
                            Notification end_noti = end_builder.build();
                            end_noti.flags = Notification.FLAG_NO_CLEAR;
                            notificationManager.notify(id, end_noti);
                            startForeground(id, end_noti);

                            //보호자에게 신고문자
                            if(byWho.equals(role) || (byWho.equals("TIMEOUT")&&role.equals("Senior"))){
                                try{
                                    SmsManager smsManager = SmsManager.getDefault();
                                    if(smsManager == null){
                                        return;
                                    }
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 k시 mm분", Locale.KOREAN);
                                    TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
                                    format.setTimeZone(tz);
                                    Date current_time = new Date();
                                    Log.d(TAG, format.format(current_time));
                                    SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                                    String msg = format.format(current_time)+ " "+preferences.getString("senior_address","")+"에서 낙상 사고를 감지했습니다. ";

                                    smsManager.sendTextMessage(preferences.getString("protector_number","0"),null,msg,null,null);

                                    //2022.08.23 데베에 기록 추가
                                    DatabaseReference reportRef = database.getReference("RECORD");
                                    String key = reportRef.push().getKey();
                                    Record rc = new Record(format.format(current_time), true, byWho);
                                    Map<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put("/record/"+key, rc.recordToMap());
                                    reportRef.updateChildren(childUpdates);

                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }else{
                            }

                            while(true){
                                Log.d(TAG,"waiting");
                                if(delete){
                                    Log.d(TAG,"delete");
                                    stopForeground(true);
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                            stopForeground(true);

                            Log.d(TAG,"done");
                            break;
                        } else {
                            progress += 1;
                            builder2.setContentText(Integer.toString(progress));
                            builder2.setProgress(TIME, progress, false);
                            builder2.setContentTitle("신고까지 "+Integer.toString(TIME-progress)+"초 남았습니다");
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
                    stopForeground(true);
                    //stopSelf();
                }catch(Exception e){

                }
            }
        });
        timer.start();


        return START_NOT_STICKY;
    }

    private String switchRole(String role){
        if(role.equals("Protector")){
            return "Senior";
        }else{
            return "Protector";
        }
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
