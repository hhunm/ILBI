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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edt_id;
    private EditText edt_password;
    private Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        toolbarInit();


        edt_id = findViewById(R.id.edt_login_id);
        edt_password = findViewById(R.id.edt_login_password);
        btn_login = findViewById(R.id.btn_login_login);
        layout_init();

    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("로그인");
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
    void layout_init() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        View view_main = findViewById(R.id.view_login);
        int padding_outside = metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        TextView id_txt = findViewById(R.id.txt_login_id);
        id_txt.setText("아이디");

        edt_id.setHint("아이디를 입력하세요");

        TextView password_txt = findViewById(R.id.txt_login_password);
        password_txt.setText("비밀번호");

        edt_password.setHint("비밀번호를 입력하세요");

        btn_login.setText("로그인");

    }
}
