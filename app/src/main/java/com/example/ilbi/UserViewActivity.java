package com.example.ilbi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RelativeLayout layout_user;
    private RelativeLayout layout_address;
    private LinearLayout layout_record;
    private SharedPreferences preferences;
    private Button user_modify;
    private Button reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

        user_modify = findViewById(R.id.btn_user_info);
        reset = findViewById(R.id.btn_reset);

        toolbarInit();
        layout_init();

        user_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserViewActivity.this,"피보호자 정보 수정",Toast.LENGTH_SHORT).show();
                UserViewActivity.this.finish();
                Intent intent = new Intent(getApplicationContext(), UserModificationActivity.class);
                startActivity(intent);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserViewActivity.this,"reset",Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
            }
        });
    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("피보호자 정보");
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

    protected void layout_init(){
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        boolean isNormal = preferences.getBoolean("isNormal",true);
        int tSize;
        if(isNormal){
            tSize = MainActivity.TEXT_SIZE_NORMAL;
        }else{
            tSize = MainActivity.TEXT_SIZE_BIG;
        }

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        //기기 해상도 정보 가져오기
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        //메인 패딩 설정
        View view_main = findViewById(R.id.view_user);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        //user 뷰 패딩 설정
        layout_user = findViewById(R.id.layout_user);

        //user 이미지 뷰
        ImageView user_image = findViewById(R.id.img_user);
        RelativeLayout.LayoutParams user_img_pr = (RelativeLayout.LayoutParams) user_image.getLayoutParams();
        user_image.setImageResource(R.drawable.senior_image);
        user_img_pr.width = metrics.widthPixels / 5;
        user_img_pr.height = metrics.widthPixels / 5;
        int margin_inside= metrics.widthPixels / 100 * 2;
        user_img_pr.setMarginEnd(margin_inside);

        //user 타이틀 텍스트 뷰
        TextView user_title = findViewById(R.id.txt_user_title);
        user_title.setText("피보호자");
        user_title.setTextSize(Dimension.SP, tSize);

        //user 내용 텍스트 뷰
        TextView user_content = findViewById(R.id.txt_user_name);
        user_content.setText(preferences.getString("senior_name","등록된 이름이 없습니다."));
        user_content.setTextSize(Dimension.SP, tSize);

        TextView user_number = findViewById(R.id.txt_user_number);
        user_number.setText(preferences.getString("senior_number","등록된 번호가 없습니다."));
        user_number.setTextSize(Dimension.SP, tSize);

        //address 뷰 패딩 설정
        layout_address = findViewById(R.id.layout_address);

        //address 타이틀 텍스트 뷰
        TextView address_title = findViewById(R.id.txt_address_title);
        address_title.setText("주소");
        address_title.setTextSize(Dimension.SP, tSize);

        //address 내용 텍스트 뷰
        TextView address_content = findViewById(R.id.txt_address_content);
        address_content.setText(preferences.getString("senior_address","등록된 주소가 없습니다."));
        address_content.setTextSize(Dimension.SP, tSize);

        //ip
        TextView ip_title = findViewById(R.id.txt_ip_title_usr);
        ip_title.setText("카메라 ip 주소");
        ip_title.setTextSize(Dimension.SP, tSize);

        //address 내용 텍스트 뷰
        TextView ip_content = findViewById(R.id.txt_ip_content_usr);
        ip_content.setText(preferences.getString("camera_ip","등록된 ip주소가 없습니다."));
        ip_content.setTextSize(Dimension.SP, tSize);

        //글씨 크기 뷰
        TextView font_size_title = findViewById(R.id.txt_font_size_title);
        font_size_title.setText("글씨 크기");
        font_size_title.setTextSize(Dimension.SP, tSize);

        TextView font_size_content = findViewById(R.id.txt_font_size_content);
        if(preferences.getBoolean("isNormal",true)){
            font_size_content.setText("보통");
        }else{
            font_size_content.setText("크게");
        }
        font_size_content.setTextSize(Dimension.SP, tSize);

        //수정 버튼
        user_modify.setText("수정");
        user_modify.setTextSize(Dimension.SP, tSize);

        //record 뷰 패딩 설정
        layout_record = findViewById(R.id.layout_record);

        //낙상 타이틀 텍스트 뷰
        TextView record_title = findViewById(R.id.txt_record_title);
        record_title.setText("낙상기록");
        record_title.setTextSize(Dimension.SP, tSize);


        TextView record1 = findViewById(R.id.txt_record1);
        record1.setText("낙상기록이 없습니다");
        record1.setTextSize(Dimension.SP, tSize);



    }
}
