package com.example.ilbi;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StartGeneralActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button next_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_gen_layout);

        toolbarInit();
        layout_init();

    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("일비 시작하기");
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

    void layout_init(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        View view_main = findViewById(R.id.view_start_gn);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        TextView name_txt = findViewById(R.id.txt_name_gn);
        name_txt.setText("이름");

        EditText name_edit = findViewById(R.id.edit_name_gn);
        name_edit.setHint("이름을 입력하세요");

        TextView number_txt = findViewById(R.id.txt_number_gn);
        number_txt.setText("번호");

        EditText number_edit = findViewById(R.id.edit_number_gn);
        number_edit.setHint("번호를 입력하세요");

        TextView role_title = findViewById(R.id.txt_title_role);
        role_title.setText("역할");

        TextView role_content = findViewById(R.id.txt_content_role);
        role_content.setText("당신은 보호자인가요?");

        RadioButton protector_rbtn = findViewById(R.id.rbtn_protector);
        protector_rbtn.setText("보호자");

        RadioButton senior_rbtn = findViewById(R.id.rbtn_senior);
        senior_rbtn.setText("피보호자");

        next_btn = findViewById(R.id.btn_start_gn);
        next_btn.setText("다음");
    }
}
