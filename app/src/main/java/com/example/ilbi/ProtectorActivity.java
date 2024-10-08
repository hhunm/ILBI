package com.example.ilbi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProtectorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RelativeLayout layout_protector_info;
    private Button protector_modify;
    private SharedPreferences preferences;
    private User user = User.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_layout);

        toolbarInit();
        layout_init();

        protector_modify.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(ProtectorActivity.this,"보호자 정보 수정",Toast.LENGTH_SHORT).show();
                ProtectorActivity.this.finish();
                Intent intent = new Intent(getApplicationContext(), ProtectorModificationActivity.class);
                startActivity(intent);

            }
        });
    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("보호자");
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
        View view_main = findViewById(R.id.view_protector);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        //protector_info 뷰 패딩 설정
        layout_protector_info = findViewById(R.id.layout_protector_info);

        //protector_info 이미지 뷰
        ImageView user_image = findViewById(R.id.img_protector_info);
        RelativeLayout.LayoutParams user_img_pr = (RelativeLayout.LayoutParams) user_image.getLayoutParams();
        user_image.setImageResource(R.drawable.protector_image);
        user_img_pr.width = metrics.widthPixels / 5;
        user_img_pr.height = metrics.widthPixels / 5;
        int margin_inside= metrics.widthPixels / 100 * 2;
        user_img_pr.setMarginEnd(margin_inside);

        //protector_info 타이틀 텍스트 뷰
        TextView protector_title = findViewById(R.id.txt_protector_info_title);
        protector_title.setText("보호자");
        protector_title.setTextSize(Dimension.SP, tSize);

        //protector_info 이름
        TextView protector_name = findViewById(R.id.txt_protector_info_name);
        String pName;
        if(user.getProtector_name() == null){
            pName = "등록된 보호자가 없습니다";
        }else{
            pName = user.getProtector_name();
        }
        protector_name.setText(pName);
        protector_name.setTextSize(Dimension.SP, tSize);

        //protector_info 번호
        TextView protector_number = findViewById(R.id.txt_protector_info_number);
        String pNum;
        if(user.getProtector_number() == null){
            pNum = "등록된 보호자가 없습니다";
        }else{
            pNum = user.getProtector_number();
        }
        protector_number.setText(pNum);
        protector_number.setTextSize(Dimension.SP, tSize);

        //수정 버튼
        protector_modify = findViewById(R.id.btn_protector_info);
        if(user.getMy_role()){
            protector_modify.setVisibility(View.GONE);
        }
        protector_modify.setText("보호자 정보 수정");
        protector_modify.setTextSize(Dimension.SP, tSize);
    }
}
