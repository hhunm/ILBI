package com.example.ilbi;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CameraActivity extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout layout_phonecall;
    RelativeLayout layout_guide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);

        toolbarInit();
        layout_init();
    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("실시간 모니터링");
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
    protected void layout_init() {
        //기기 해상도 정보 가져오기
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        //메인 패딩 설정
        View view_main = findViewById(R.id.view_camera);
        int padding_outside = metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        //phonecall 뷰 패딩 설정
        layout_phonecall = findViewById(R.id.layout_phonecall);
        int padding_unitL = metrics.widthPixels / 100 * 4;
        layout_phonecall.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);

        //phonecall 이미지 뷰
        ImageView phonecall_image = findViewById(R.id.img_phonecall);
        RelativeLayout.LayoutParams phonecall_img_pr = (RelativeLayout.LayoutParams) phonecall_image.getLayoutParams();
        phonecall_image.setImageResource(R.drawable.phone_call_image);
        phonecall_img_pr.width = metrics.widthPixels / 7;
        phonecall_img_pr.height = metrics.widthPixels / 7;
        int margin_inside = metrics.widthPixels / 100 * 2;
        phonecall_img_pr.setMarginEnd(margin_inside);
        phonecall_img_pr.topMargin = metrics.heightPixels / 100 * 1;

        //phonecall 타이틀 텍스트 뷰
        TextView phonecall_title = findViewById(R.id.txt_phonecall_title);
        phonecall_title.setText("통화");

       //phonecall 내용 텍스트 뷰
        TextView phonecall_content = findViewById(R.id.txt_phonecall_content);
        phonecall_content.setText("이소민");

        //guide 뷰 패딩 설정
        layout_guide = findViewById(R.id.layout_guide);
        layout_guide.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);

        //guide 타이틀 텍스트 뷰
        TextView guide_title = findViewById(R.id.txt_guide_title);
        RelativeLayout.LayoutParams guide_title_pr = (RelativeLayout.LayoutParams) guide_title.getLayoutParams();
        guide_title.setText("119 신고 방법");
        guide_title_pr.setMarginEnd(margin_inside);
        guide_title_pr.topMargin = metrics.heightPixels / 100 * 1;
        guide_title_pr.bottomMargin = metrics.heightPixels / 100 * 1;

        //guide 내용 텍스트 뷰
        TextView address_content = findViewById(R.id.txt_guide_content);
        address_content.setText("1. 침착하게 119에 신고해주세요\n" +
                "2. 사고 위치를 알려주세요\n" +
                "3. 사고상황을 알려주세요\n" +
                "4. 전화를 끊지말고 119의 지시에 따라주세요\n" +
                "5. 전화를 잘 받아주세요");
    }
}
