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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserViewActivity extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout layout_user;
    RelativeLayout layout_address;
    LinearLayout layout_record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

        toolbarInit();
        layout_init();

    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("내 정보");
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
        //기기 해상도 정보 가져오기
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Log.d("fall",Integer.toString(metrics.widthPixels));

        //메인 패딩 설정
        View view_main = findViewById(R.id.view_fall);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        //user 뷰 패딩 설정
        layout_user = findViewById(R.id.layout_user);
        int padding_unitL=  metrics.widthPixels / 100 * 4;
        layout_user.setPadding(padding_unitL, padding_unitL, padding_unitL, padding_unitL);

        //user 이미지 뷰
        ImageView user_image = findViewById(R.id.img_user);
        RelativeLayout.LayoutParams user_img_pr = (RelativeLayout.LayoutParams) user_image.getLayoutParams();
        user_image.setImageResource(R.drawable.senior_image);
        user_img_pr.width = metrics.widthPixels / 6;
        user_img_pr.height = metrics.widthPixels / 6;
        int margin_inside= metrics.widthPixels / 100 * 2;
        user_img_pr.setMarginEnd(margin_inside);
        user_img_pr.topMargin = metrics.heightPixels / 100 * 2;

        //user 타이틀 텍스트 뷰
        TextView user_title = findViewById(R.id.txt_user_title);
        user_title.setText("이름");

        //user 내용 텍스트 뷰
        TextView user_content = findViewById(R.id.txt_user_content);
        user_content.setText("이현민");

        //address 뷰 패딩 설정
        layout_address = findViewById(R.id.layout_address);
        layout_address.setPadding(padding_unitL*2, padding_unitL, padding_unitL*2, padding_unitL);

        //address 타이틀 텍스트 뷰
        TextView address_title = findViewById(R.id.txt_address_title);
        RelativeLayout.LayoutParams user_title_pr = (RelativeLayout.LayoutParams) address_title.getLayoutParams();
        address_title.setText("주소");
        user_title_pr.setMarginEnd(margin_inside);
        user_title_pr.topMargin = metrics.heightPixels / 100 * 1;

        //address 내용 텍스트 뷰
        TextView address_content = findViewById(R.id.txt_address_content);
        address_content.setText("서울특별시 용산구 abc로 1가 101동 1504호");

        //record 뷰 패딩 설정
        layout_record = findViewById(R.id.layout_record);
        layout_record.setPadding(padding_unitL*2, padding_unitL, padding_unitL*2, padding_unitL);

        //address 타이틀 텍스트 뷰
        TextView record_title = findViewById(R.id.txt_record_title);
        RelativeLayout.LayoutParams record_title_pr = (RelativeLayout.LayoutParams) address_title.getLayoutParams();
        record_title.setText("낙상기록");
        record_title_pr.setMarginEnd(margin_inside);
        record_title_pr.topMargin = metrics.heightPixels / 100 * 1;
        record_title_pr.setMarginEnd(margin_inside);

        TextView record1 = findViewById(R.id.txt_record1);
        record1.setText("낙상기록이 없습니다");



    }
}
