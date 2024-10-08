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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StartGeneralActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button next_btn;
    private EditText edit_name;
    private EditText edit_number;
    private RadioButton rb_senior;
    private RadioButton rb_protector;
    private final String TAG = "StartGeneralActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_gen_layout);

        toolbarInit();
        layout_init();

        edit_name = findViewById(R.id.edit_id_gn);
        edit_number = findViewById(R.id.edit_password_gn);
        rb_senior = findViewById(R.id.rbtn_senior);
        rb_protector = findViewById(R.id.rbtn_protector);

        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edit_name.getText().toString();
                String password = edit_number.getText().toString();
                String role = null;
                User user = User.getInstance();

                SharedPreferences.Editor editor = preferences.edit();

                Intent intent;

                if(id.length() == 0 || password.length() == 0){
                    Toast.makeText(StartGeneralActivity.this,"입력하지 않은 부분이 있습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }


                //new
                user.setMy_id(id);
                user.setMy_password(password);

                if(rb_senior.isChecked()){
//                    role = "Senior";
//                    editor.putString("senior_name", id);
//                    editor.putString("senior_number", password);
//                    editor.putString("role", role);


                    user.setMy_role(true);
                    intent = new Intent(StartGeneralActivity.this, StartSeniorActivity.class);

                }else if(rb_protector.isChecked()){
//                    role = "Protector";
//                    editor.putString("protector_name", id);
//                    editor.putString("protector_number", password);
//                    editor.putString("role", role);

                    //new
                    user.setMy_role(false);

                    intent = new Intent(StartGeneralActivity.this, StartProtectorActivity.class);

                }else{
                    Toast.makeText(StartGeneralActivity.this,"역할을 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "id: "+ user.getMy_id());
                Log.d(TAG, "pw: "+ user.getMy_password());
                Log.d(TAG, "isSenior: "+ user.getMy_role());
//
//                editor.commit();
//
//                Log.d(TAG, "senior_name: " + preferences.getString("senior_name",""));
//                Log.d(TAG, "senior_number: " + preferences.getString("senior_number",""));
//                Log.d(TAG, "protector_name: " + preferences.getString("protector_name",""));
//                Log.d(TAG, "protector_number: " + preferences.getString("protector_number",""));
//                Log.d(TAG, "camera_ip: " + preferences.getString("camera_ip",""));

                user.checkIdDuplicated(id, getApplicationContext(), intent);
            }
        });

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

        TextView name_txt = findViewById(R.id.txt_id_gn);
        name_txt.setText("아이디");

        EditText name_edit = findViewById(R.id.edit_id_gn);
        name_edit.setHint("아이디을 입력하세요");

        TextView number_txt = findViewById(R.id.txt_password_gn);
        number_txt.setText("비밀번호");

        EditText number_edit = findViewById(R.id.edit_password_gn);
        number_edit.setHint("비밀번호를 입력하세요");

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
