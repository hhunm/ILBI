package com.example.ilbi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserModificationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SharedPreferences preferences;
    private EditText user_name;
    private EditText user_number;
    private EditText user_address;
    private Button user_modify;
    private RadioButton rb_normal;
    private RadioButton rb_big;
    private EditText ip1;
    private EditText ip2;
    private EditText ip3;
    private EditText ip4;

    private FirebaseDatabase database;
    private User user = User.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_modification_layout);

        user_name = findViewById(R.id.txt_user_name_modify);
        user_number = findViewById(R.id.txt_user_number_modify);
        user_address = findViewById(R.id.txt_address_title_modify);
        rb_normal = findViewById(R.id.rbtn_normal);
        rb_big = findViewById(R.id.rbtn_big);

        ip1 = findViewById(R.id.edit_ip1_modify);
        ip2 = findViewById(R.id.edit_ip2_modify);
        ip3 = findViewById(R.id.edit_ip3_modify);
        ip4 = findViewById(R.id.edit_ip4_modify);

        toolbarInit();
        layout_init();

        user_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String name = user_name.getText().toString();
                String address = user_address.getText().toString();
                String number = user_number.getText().toString();

                ArrayList<String> ip = new ArrayList<String>(4);
                ip.add(ip1.getText().toString());
                ip.add(ip2.getText().toString());
                ip.add(ip3.getText().toString());
                ip.add(ip4.getText().toString());


//                editor.putString("senior_name", name);
//                editor.putString("senior_number", number);
//                editor.putString("senior_address", address);

                database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("USER");

                myRef.child(user.getMy_id()).child("INFO").child("Name").setValue(name);
                user.setSenior_name(name);
                myRef.child(user.getMy_id()).child("INFO").child("Number").setValue(number);
                user.setSenior_number(number);
                myRef.child(user.getMy_id()).child("INFO").child("Address").setValue(address);
                user.setSenior_address(address);

                if(rb_normal.isChecked()){
                    editor.putBoolean("isNormal",true);
                }else if(rb_big.isChecked()){
                    editor.putBoolean("isNormal",false);
                }else{
                    Toast.makeText(UserModificationActivity.this,"글씨크기를 선택해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                String camera_ip = "";

                for(int i = 0; i < 4 ; i++){
                    if(ip.get(i).length() == 0){
                        Toast.makeText(UserModificationActivity.this,"올바른 ip를 입력해주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    camera_ip = camera_ip.concat(ip.get(i));
                    camera_ip = camera_ip.concat(".");
                }
                camera_ip = camera_ip.substring(0, camera_ip.length() - 1);
                //editor.putString("camera_ip",camera_ip);

                myRef.child(user.getMy_id()).child("INFO").child("IP").setValue(camera_ip);
                user.setIp(camera_ip);

                editor.commit();

                UserModificationActivity.this.finish();
                Intent intent = new Intent(getApplicationContext(), UserViewActivity.class);
                startActivity(intent);
            }
        });



    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("피보호자 정보 수정");
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
    private void layout_init() {
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        boolean isNormal = preferences.getBoolean("isNormal",true);
        int tSize;
        if(isNormal){
            tSize = MainActivity.TEXT_SIZE_NORMAL;
        }else{
            tSize = MainActivity.TEXT_SIZE_BIG;
        }


        //기기 해상도 정보 가져오기
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        //메인 패딩 설정
        View view_main = findViewById(R.id.view_user_modify);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        //protector_info 뷰 패딩 설정

        //protector_info 이미지 뷰
        ImageView user_image_m = findViewById(R.id.img_user_modify);
        RelativeLayout.LayoutParams user_img_pr_m = (RelativeLayout.LayoutParams) user_image_m.getLayoutParams();
        user_image_m.setImageResource(R.drawable.senior_image);
        user_img_pr_m.width = metrics.widthPixels / 5;
        user_img_pr_m.height = metrics.widthPixels / 5;
        int margin_inside= metrics.widthPixels / 100 * 2;
        user_img_pr_m.setMarginEnd(margin_inside);

        //protector_info 타이틀 텍스트 뷰
        TextView user_title = findViewById(R.id.txt_user_title_modify);
        user_title.setText("피보호자");
        user_title.setTextSize(Dimension.SP, tSize);

        //protector_info 이름
        String sName;
        if(user.getSenior_name() ==  null){
            sName = "등록된 이름이 없습니다.";
        }else{
            sName = user.getSenior_name();
        }
        user_name.setText(sName);
        user_name.setTextSize(Dimension.SP, tSize);

        //protector_info 번호
        String sNum;
        if(user.getSenior_number() ==  null){
            sNum = "등록된 번호가 없습니다.";
        }else{
            sNum = user.getSenior_number();
        }
        user_number.setText(sNum);
        user_number.setTextSize(Dimension.SP, tSize);
        //주소 뷰 패딩 설정

        //주소
        TextView address_title = findViewById(R.id.txt_user_address_modify);
        address_title.setText("주소");
        address_title.setTextSize(Dimension.SP, tSize);

        String address;
        if(user.getSenior_address() ==  null){
            address = "등록된 주소가 없습니다.";
        }else{
            address = user.getSenior_address();
        }
        user_address.setText(address);
        user_address.setTextSize(Dimension.SP, tSize);

        //ip
        TextView ip_title= findViewById(R.id.txt_ip_title_usr_modify);
        ip_title.setText("카메라 ip 주소");
        ip_title.setTextSize(Dimension.SP, tSize);

        String ip_address = user.getIp();
        String[] ip = ip_address.split("\\.");

        ip1.setText(ip[0]);
        ip2.setText(ip[1]);
        ip3.setText(ip[2]);
        ip4.setText(ip[3]);

        ip1.setTextSize(Dimension.SP, tSize);
        ip2.setTextSize(Dimension.SP, tSize);
        ip3.setTextSize(Dimension.SP, tSize);
        ip4.setTextSize(Dimension.SP, tSize);


        TextView font_size_title = findViewById(R.id.txt_font_size_title_modify);
        font_size_title.setText("글씨 크기");
        font_size_title.setTextSize(Dimension.SP, tSize);


        rb_normal.setText("보통");
        rb_normal.setTextSize(Dimension.SP, tSize);
        rb_normal.setChecked(true);
        rb_big.setText("크게");
        rb_big.setTextSize(Dimension.SP, tSize);


        //수정 버튼
        user_modify = findViewById(R.id.btn_user_modify);
        user_modify.setText("수정");
        user_modify.setTextSize(Dimension.SP, tSize);
    }
}
