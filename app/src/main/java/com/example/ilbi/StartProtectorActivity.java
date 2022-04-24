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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StartProtectorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private final String TAG = "StartProtectorActivity";
    private Button submit_btn;
    private EditText sn_address_edit;
    private EditText sn_name_edit;
    private EditText sn_number_edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_protector_layout);
        toolbarInit();

        sn_address_edit = findViewById(R.id.edit_sn_address);
        sn_name_edit = findViewById(R.id.edit_sn_name);
        sn_number_edit = findViewById(R.id.edit_sn_number);
        submit_btn = findViewById(R.id.btn_pt_submit);
        layout_init();

        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = sn_address_edit.getText().toString();
                String sn_name = sn_name_edit.getText().toString();
                String sn_number = sn_number_edit.getText().toString();
                SharedPreferences.Editor editor = preferences.edit();
                Intent intent;

                if(address.length() == 0 || sn_name.length() == 0 || sn_number.length() == 0){
                    Toast.makeText(StartProtectorActivity.this,"입력하지 않은 부분이 있습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    editor.putString("senior_address", address);
                    editor.putString("senior_name", sn_name);
                    editor.putString("senior_number", sn_number);
                    intent = new Intent(StartProtectorActivity.this, MainActivity.class);
                    editor.putBoolean("isFirst", false);
                    editor.commit();
                }

                Log.d(TAG, "senior_name: " + preferences.getString("senior_name",""));
                Log.d(TAG, "senior_number: " + preferences.getString("senior_number",""));
                Log.d(TAG, "protector_name: " + preferences.getString("protector_name",""));
                Log.d(TAG, "protector_number: " + preferences.getString("protector_number",""));
                Log.d(TAG, "protector_number: " + preferences.getString("senior_address",""));
                Log.d(TAG, "isFirst: " + preferences.getBoolean("isFirst",true));

                Toast.makeText(StartProtectorActivity.this, "완료", Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
    private void layout_init() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        View view_main = findViewById(R.id.view_start_pt);
        int padding_outside= metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        TextView pt_name_txt = findViewById(R.id.txt_sn_name);
        pt_name_txt.setText("피보호자의 이름");

        sn_name_edit.setHint("피보호자의 이름을 입력하세요");

        TextView pt_number_txt = findViewById(R.id.txt_sn_number);
        pt_number_txt.setText("피보호자의 번호");

        sn_number_edit.setHint("피보호자의 번호를 입력하세요");

        TextView address_txt = findViewById(R.id.txt_sn_address);
        address_txt.setText("피보호자의 주소");


        sn_address_edit.setHint("피보호자의 주소를 입력하세요");

        submit_btn.setText("완료");
    }
}
