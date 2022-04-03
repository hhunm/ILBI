package com.example.ilbi;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProtectorActivity extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout layout_protector_info;
    Button protector_modify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_layout);

        toolbarInit();
        layout_init();

        protector_modify.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(ProtectorActivity.this,"수정",Toast.LENGTH_SHORT).show();
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
        //기기 해상도 정보 가져오기
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Log.d("fall",Integer.toString(metrics.widthPixels));

        //메인 패딩 설정
        View view_main = findViewById(R.id.view_protector);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        //protector_info 뷰 패딩 설정
        layout_protector_info = findViewById(R.id.layout_protector_info);
        int padding_unitL=  metrics.widthPixels / 100 * 4;
        layout_protector_info.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);

        //protector_info 이미지 뷰
        ImageView user_image = findViewById(R.id.img_protector_info);
        RelativeLayout.LayoutParams user_img_pr = (RelativeLayout.LayoutParams) user_image.getLayoutParams();
        user_image.setImageResource(R.drawable.protector_image);
        user_img_pr.width = metrics.widthPixels / 5;
        user_img_pr.height = metrics.widthPixels / 5;
        int margin_inside= metrics.widthPixels / 100 * 2;
        user_img_pr.setMarginEnd(margin_inside);
        user_img_pr.topMargin = metrics.heightPixels / 100 * 2;

        //protector_info 타이틀 텍스트 뷰
        TextView protector_title = findViewById(R.id.txt_protector_info_title);
        protector_title.setText("보호자");

        //protector_info 이름
        TextView protector_name = findViewById(R.id.txt_protector_info_name);
        protector_name.setText("이소민");

        //protector_info 번호
        TextView protector_number = findViewById(R.id.txt_protector_info_number);
        protector_number.setText("010 2222 3333");

        //수정 버튼
        protector_modify = findViewById(R.id.btn_protector_info);
        protector_modify.setText("보호자 정보 수정");
    }
}
