package com.example.ilbi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class CameraActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RelativeLayout layout_phonecall;
    private RelativeLayout layout_guide;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);

        toolbarInit();
        layout_init();

        //전화 걸기
        layout_phonecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CameraActivity.this,"전화걸기",Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                String number = preferences.getString("senior_number","abc");

                Intent it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(it);
            }
        });
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
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        boolean isNormal = preferences.getBoolean("isNormal",true);
        int tSize;
        if(isNormal){
            tSize = MainActivity.TEXT_SIZE_NORMAL;
        }else{
            tSize = MainActivity.TEXT_SIZE_BIG;
        }
        String role = preferences.getString("role","");

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
        layout_guide = findViewById(R.id.layout_guide);

        //phonecall 이미지 뷰
        ImageView phonecall_image = findViewById(R.id.img_phonecall);
        RelativeLayout.LayoutParams phonecall_img_pr = (RelativeLayout.LayoutParams) phonecall_image.getLayoutParams();
        phonecall_image.setImageResource(R.drawable.phone_call_image);
        phonecall_img_pr.width = metrics.widthPixels / 7;
        phonecall_img_pr.height = metrics.widthPixels / 7;
        int margin_inside = metrics.widthPixels / 100 * 2;
        phonecall_img_pr.setMarginEnd(margin_inside);

        if(role.equals("Protector")){
            //phonecall 타이틀 텍스트 뷰
            TextView phonecall_title = findViewById(R.id.txt_phonecall_title);
            phonecall_title.setText("통화");
            phonecall_title.setTextSize(Dimension.SP, tSize);

            //phonecall 내용 텍스트 뷰
            TextView phonecall_content = findViewById(R.id.txt_phonecall_content);
            phonecall_content.setText(preferences.getString("senior_name",""));
            phonecall_content.setTextSize(Dimension.SP, tSize);

            //guide 뷰 패딩 설정
            layout_guide = findViewById(R.id.layout_guide);

            //guide 타이틀 텍스트 뷰
            TextView guide_title = findViewById(R.id.txt_guide_title);
            RelativeLayout.LayoutParams guide_title_pr = (RelativeLayout.LayoutParams) guide_title.getLayoutParams();
            guide_title.setText("119 신고 방법");
            guide_title_pr.setMarginEnd(margin_inside);
            guide_title.setTextSize(Dimension.SP, tSize);

            //guide 내용 텍스트 뷰
            TextView address_content = findViewById(R.id.txt_guide_content);
            address_content.setText("1. 침착하게 119에 신고해주세요\n" +
                    "2. 사고 위치를 알려주세요\n" +
                    "3. 사고상황을 알려주세요\n" +
                    "4. 전화를 끊지말고 119의 지시에 따라주세요\n" +
                    "5. 전화를 잘 받아주세요");
            address_content.setTextSize(Dimension.SP, tSize);
        }else{
            LinearLayout v = findViewById(R.id.view_camera_for_protector);
            v.removeAllViews();
        }

        WebView wb_camera = findViewById(R.id.wb_camera);
        wb_camera.setWebViewClient(new WebViewClient());
        wb_camera.setBackgroundColor(255);

        WebSettings webSettings = wb_camera.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        wb_camera.loadUrl("http://"+preferences.getString("camera_ip",null)+":9999/?action=stream");

    }
}
