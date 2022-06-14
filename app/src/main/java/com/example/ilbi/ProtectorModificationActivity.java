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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProtectorModificationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RelativeLayout layout_protector_info;
    private Button protector_modify;
    private SharedPreferences preferences;
    private EditText protector_name;
    private EditText protector_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_modification_layout);
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        protector_number = findViewById(R.id.txt_protector_info_number_modify);
        protector_name = findViewById(R.id.txt_protector_info_name_modify);

        toolbarInit();
        layout_init();

        protector_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String name = protector_name.getText().toString();
                String number = protector_number.getText().toString();

                editor.putString("protector_name", name);
                editor.putString("protector_number", number);

                editor.commit();

                ProtectorModificationActivity.this.finish();
                Intent intent = new Intent(getApplicationContext(), ProtectorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("보호자 정보 수정");
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
        View view_main = findViewById(R.id.view_protector_modify);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        //protector_info 뷰 패딩 설정
        layout_protector_info = findViewById(R.id.layout_protector_info_modify);

        //protector_info 이미지 뷰
        ImageView user_image_m = findViewById(R.id.img_protector_info_modify);
        RelativeLayout.LayoutParams user_img_pr_m = (RelativeLayout.LayoutParams) user_image_m.getLayoutParams();
        user_image_m.setImageResource(R.drawable.protector_image);
        user_img_pr_m.width = metrics.widthPixels / 5;
        user_img_pr_m.height = metrics.widthPixels / 5;
        int margin_inside= metrics.widthPixels / 100 * 2;
        user_img_pr_m.setMarginEnd(margin_inside);

        //protector_info 타이틀 텍스트 뷰
        TextView protector_title = findViewById(R.id.txt_protector_info_title_modify);
        protector_title.setText("보호자");
        protector_title.setTextSize(Dimension.SP, tSize);

        //protector_info 이름
        protector_name.setText(preferences.getString("protector_name","등록된 보호자가 없습니다"));
        protector_name.setTextSize(Dimension.SP, tSize);

        //protector_info 번호
        protector_number.setText(preferences.getString("protector_number","등록된 보호자가 없습니다"));
        protector_number.setTextSize(Dimension.SP, tSize);

        //수정 버튼
        protector_modify = findViewById(R.id.btn_protector_info_modify);
        protector_modify.setText("수정");
        protector_modify.setTextSize(Dimension.SP, tSize);
    }
}
