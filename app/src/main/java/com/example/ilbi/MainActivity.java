package com.example.ilbi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout layout_fall;
    RelativeLayout layout_camera;
    RelativeLayout layout_call;
    RelativeLayout layout_cancel;
    RelativeLayout layout_protector;
    RelativeLayout layout_setting;
    RelativeLayout layout_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        //액션바 없애기
//        ActionBar actionbar = getSupportActionBar();
//        actionbar.hide();

        //툴바
        toolbarInit();
        //레이아웃
        layoutInit();

        //camera 클릭 이벤트
        layout_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"camera",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
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
                //Toast.makeText(getApplicationContext(), "마이페이지", Toast.LENGTH_LONG).show();
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
    private void layoutInit(){
        //기기 해상도 정보 가져오기
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Log.d("main",Integer.toString(metrics.widthPixels));

        //메인 패딩 설정
        View view_main = findViewById(R.id.view_fall);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        //fall 뷰 패딩 설정
        layout_fall = findViewById(R.id.layout_fall);
        int padding_unitL=  metrics.widthPixels / 100 * 4;
        layout_fall.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);

        //fall 이미지 뷰
        ImageView fall_image = findViewById(R.id.img_fall);
        RelativeLayout.LayoutParams fall_img_pr = (RelativeLayout.LayoutParams) fall_image.getLayoutParams();
        fall_image.setImageResource(R.drawable.fall_image);
        fall_img_pr.width = metrics.widthPixels / 6;
        fall_img_pr.height = metrics.widthPixels / 6;
        int margin_inside= metrics.widthPixels / 100 * 2;
        fall_img_pr.setMarginEnd(margin_inside);
        fall_img_pr.topMargin = metrics.heightPixels / 100 * 2;

        //fall 타이틀 텍스트 뷰
        TextView fall_title = findViewById(R.id.txt_fall_title);
        fall_title.setText("낙상 감지가 중단되었습니다");

        //fall 내용 텍스트 뷰
        TextView fall_content = findViewById(R.id.txt_fall_content);
        fall_content.setText("카메라가 꺼져 있습니다");

        //fall 스위치
        Switch fall_switch = findViewById(R.id.switch_fall);
        RelativeLayout.LayoutParams fall_switch_pr = (RelativeLayout.LayoutParams) fall_switch.getLayoutParams();
        fall_switch_pr.width = 320;
        fall_switch_pr.height = 40;
        fall_switch.setText("OFF");

        //camera 뷰 패딩 설정
        layout_camera = findViewById(R.id.layout_camera);
        layout_camera.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);

        //camera 이미지 뷰
        ImageView camera_image = findViewById(R.id.img_camera);
        RelativeLayout.LayoutParams camera_img_pr = (RelativeLayout.LayoutParams) camera_image.getLayoutParams();
        camera_image.setImageResource(R.drawable.camera_image);
        camera_img_pr.width = metrics.widthPixels / 6;
        camera_img_pr.height = metrics.widthPixels / 6;
        camera_img_pr.setMarginEnd(margin_inside);
        camera_img_pr.topMargin = metrics.heightPixels / 100 * 2;

        //camera 타이틀 텍스트 뷰
        TextView camera_title = findViewById(R.id.txt_camera_title);
        camera_title.setText("실시간 모니터링");

        //camera 내용 텍스트 뷰
        TextView camera_content = findViewById(R.id.txt_camera_content);
        camera_content.setText("0명의 보호자가 영상을 시청하고 있습니다");

        //camera 인원수 텍스트 뷰
        TextView camera_num = findViewById(R.id.txt_camera_num);
        camera_num.setText("0명");

        //call 뷰 패딩 설정
        layout_call = findViewById(R.id.layout_call);
        layout_call.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);

        //call 이미지 뷰
        ImageView call_image = findViewById(R.id.img_call);
        RelativeLayout.LayoutParams call_img_pr = (RelativeLayout.LayoutParams) call_image.getLayoutParams();
        call_image.setImageResource(R.drawable.call_image);
        call_img_pr.width = metrics.widthPixels / 7;
        call_img_pr.height = metrics.widthPixels / 7;
        call_img_pr.setMarginEnd(margin_inside);
        call_img_pr.topMargin = metrics.heightPixels / 100 * 2;

        //call 타이틀 텍스트 뷰
        TextView call_title = findViewById(R.id.txt_call_title);
        call_title.setText("긴급호출");

        //call 내용 텍스트 뷰
        TextView call_content = findViewById(R.id.txt_call_content);
        call_content.setText("119에 긴급신고 문자를 보냅니다");

        //cancel 뷰 패딩 설정
        layout_cancel = findViewById(R.id.layout_cancel);
        layout_cancel.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);

        //cancel 이미지 뷰
        ImageView cancel_image = findViewById(R.id.img_cancel);
        RelativeLayout.LayoutParams cancel_img_pr = (RelativeLayout.LayoutParams) cancel_image.getLayoutParams();
        cancel_image.setImageResource(R.drawable.cancel_image);
        cancel_img_pr.width = metrics.widthPixels / 7;
        cancel_img_pr.height = metrics.widthPixels / 7;
        cancel_img_pr.setMarginEnd(margin_inside);
        cancel_img_pr.topMargin = metrics.heightPixels / 100 * 2;

        //cancel 타이틀 텍스트 뷰
        TextView cancel_title = findViewById(R.id.txt_cancel_title);
        cancel_title.setText("신고 취소");

        //cancel 내용 텍스트 뷰
        TextView cancel_content = findViewById(R.id.txt_cancel_content);
        cancel_content.setText("119 신고를 취소합니다");


        //protector 뷰 패딩 설정
        layout_protector = findViewById(R.id.layout_protector);
        layout_protector.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);

        //protector 이미지 뷰
        ImageView protector_image = findViewById(R.id.img_protector);
        RelativeLayout.LayoutParams protector_img_pr = (RelativeLayout.LayoutParams) protector_image.getLayoutParams();
        protector_image.setImageResource(R.drawable.protector_image);
        protector_img_pr.width = metrics.widthPixels / 7;
        protector_img_pr.height = metrics.widthPixels / 7;
        protector_img_pr.setMarginEnd(margin_inside);
        protector_img_pr.topMargin = metrics.heightPixels / 100 * 2;

        //protector 타이틀 텍스트 뷰
        TextView protector_title = findViewById(R.id.txt_protector_title);
        protector_title.setText("보호자");

        //protector 보호자 이름 텍스트 뷰
        TextView protector_name = findViewById(R.id.txt_protector_name);
        protector_name.setText("등록된 보호자가 없습니다");

        //protector 번호 텍스트 뷰
        TextView protector_number = findViewById(R.id.txt_protector_number);
        protector_number.setText("");

        //setting 뷰 패딩 설정
        layout_setting = findViewById(R.id.layout_setting);
        layout_setting.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);
        LinearLayout.LayoutParams setting_layout_pr = (LinearLayout.LayoutParams) layout_setting.getLayoutParams();
        setting_layout_pr.width = (metrics.widthPixels/2);

        //setting 이미지 뷰
        ImageView setting_image = findViewById(R.id.img_setting);
        RelativeLayout.LayoutParams setting_img_pr = (RelativeLayout.LayoutParams) setting_image.getLayoutParams();
        setting_image.setImageResource(R.drawable.setting_image);
        setting_img_pr.width = metrics.widthPixels / 7;
        setting_img_pr.height = metrics.widthPixels / 7;
        setting_img_pr.setMarginEnd(margin_inside);

        //setting 타이틀 텍스트 뷰
        TextView setting_title = findViewById(R.id.txt_setting_title);
        setting_title.setText("설정");

        //help 뷰 패딩 설정
        layout_help = findViewById(R.id.layout_help);
        layout_help.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);
        LinearLayout.LayoutParams help_layout_pr = (LinearLayout.LayoutParams) layout_help.getLayoutParams();
        help_layout_pr.width = (metrics.widthPixels/2);

        //help 이미지 뷰
        ImageView help_image = findViewById(R.id.img_help);
        RelativeLayout.LayoutParams help_img_pr = (RelativeLayout.LayoutParams) help_image.getLayoutParams();
        help_image.setImageResource(R.drawable.help_image);
        help_img_pr.width = metrics.widthPixels / 7;
        help_img_pr.height = metrics.widthPixels / 7;
        help_img_pr.setMarginEnd(margin_inside);

        //help 타이틀 텍스트 뷰
        TextView help_title = findViewById(R.id.txt_help_title);
        help_title.setText("도움말");


    }
}
