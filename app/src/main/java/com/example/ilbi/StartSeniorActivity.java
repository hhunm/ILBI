package com.example.ilbi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class StartSeniorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button submit_btn;
    private  EditText address_edit;
    private EditText pt_name_edit;
    private EditText pt_number_edit;
    private final String TAG = "StartSeniorActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_senior_layout);

        toolbarInit();

        EditText ip1 = findViewById(R.id.edit_ip1);
        EditText ip2 = findViewById(R.id.edit_ip2);
        EditText ip3 = findViewById(R.id.edit_ip3);
        EditText ip4 = findViewById(R.id.edit_ip4);

        address_edit = findViewById(R.id.edit_address);
        pt_name_edit = findViewById(R.id.edit_pt_name);
        pt_number_edit = findViewById(R.id.edit_pt_number);
        submit_btn = findViewById(R.id.btn_sn_submit);
        layout_init();

        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = address_edit.getText().toString();
                String sn_name = pt_name_edit.getText().toString();
                String sn_number = pt_number_edit.getText().toString();
                User user = User.getInstance();
                SharedPreferences.Editor editor = preferences.edit();
                Intent intent;

                ArrayList<String> ip = new ArrayList<String>(4);
                ip.add(ip1.getText().toString());
                ip.add(ip2.getText().toString());
                ip.add(ip3.getText().toString());
                ip.add(ip4.getText().toString());

                if(address.length() == 0 || sn_name.length() == 0 || sn_number.length() == 0){
                    Toast.makeText(StartSeniorActivity.this,"입력하지 않은 부분이 있습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }else{
//                    editor.putString("senior_address", address);
//                    editor.putString("protector_name", pt_name);
//                    editor.putString("protector_number", pt_number);

                    //new
                    user.setSenior_name(sn_name);
                    user.setSenior_number(sn_number);
                    user.setSenior_address(address);

                    intent = new Intent(StartSeniorActivity.this, ConnectActivity.class);
//                    editor.putBoolean("isFirst", false);
//                    editor.commit();
                }

                String camera_ip = "";

                for(int i = 0; i < 4 ; i++){
                    if(ip.get(i).length() == 0){
                        Toast.makeText(StartSeniorActivity.this,"올바른 ip를 입력해주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    camera_ip = camera_ip.concat(ip.get(i));
                    camera_ip = camera_ip.concat(".");
                }
                camera_ip = camera_ip.substring(0, camera_ip.length() - 1);
                //new
                user.setIp(camera_ip);
               // editor.putString("camera_ip",camera_ip);


//                Log.d(TAG, "senior_name: " + preferences.getString("senior_name",""));
//                Log.d(TAG, "senior_number: " + preferences.getString("senior_number",""));
//                Log.d(TAG, "protector_name: " + preferences.getString("protector_name",""));
//                Log.d(TAG, "protector_number: " + preferences.getString("protector_number",""));
//                Log.d(TAG, "protector_number: " + preferences.getString("senior_address",""));
//                Log.d(TAG, "isFirst: " + preferences.getBoolean("isFirst",true));

                Log.d(TAG, "id: "+ user.getMy_id());
                Log.d(TAG, "pw: "+ user.getMy_password());
                Log.d(TAG, "isSenior: "+ user.getMy_role());
                Log.d(TAG, "sn_name: "+user.getSenior_name());
                Log.d(TAG, "sn_number: " + user.getSenior_number());
                Log.d(TAG, "address"+user.getSenior_address());
                Log.d(TAG, "ip: "+user.getIp());

                //데베에 입력
                user.signUP();

                Toast.makeText(StartSeniorActivity.this, "완료", Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        });

    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("피보호자");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sub_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void layout_init(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        View view_main = findViewById(R.id.view_start_sn);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        TextView address_txt = findViewById(R.id.txt_address);
        address_txt.setText("주소");


        address_edit.setHint("주소를 입력하세요");

        TextView pt_name_txt = findViewById(R.id.txt_pt_name);
        pt_name_txt.setText("이름");

        pt_name_edit.setHint("이름을 입력하세요");

        TextView pt_number_txt = findViewById(R.id.txt_pt_number);
        pt_number_txt.setText("번호");

        pt_number_edit.setHint("번호를 입력하세요");


        TextView ip_title = findViewById(R.id.txt_ip_title_gn);
        ip_title.setText("낙상감지 시스템의 ip주소");



        submit_btn.setText("완료");

    }
}
