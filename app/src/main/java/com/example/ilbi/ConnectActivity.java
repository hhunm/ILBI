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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;
import java.util.Map;

import static java.lang.Thread.sleep;

public class ConnectActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText code;
    private Button btn;
    private Button btn2;
    private final String TAG = "ConnectActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_layout);
        toolbarInit();

        User user = User.getInstance();
        code = findViewById(R.id.edt_connect_token);
        btn = findViewById(R.id.btn_connect);
        btn2 = findViewById(R.id.btn_connect2);

        layout_init();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_token = code.getText().toString();
                user.checkToken(input_token);

            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getTokenChecked()) {
                    user.checkPartner(getApplicationContext());
                }else{
                    Toast.makeText(getApplicationContext(),"코드를 먼저 입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("연동");
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

        View view_main = findViewById(R.id.view_connect);
        int padding_outside = metrics.heightPixels / 100 * 2;
        view_main.setPadding(padding_outside, padding_outside, padding_outside, padding_outside);

        TextView outline =findViewById(R.id.txt_connect_outline);
        outline.setText("계정 연동을 위해 서로의 코드를 입력해주세요.");

        TextView id_title =findViewById(R.id.txt_connect_id);
        id_title.setText("내 코드");

        User user = User.getInstance();
        TextView my_token = findViewById(R.id.txt_my_token);
        my_token.setText(user.makeToken());

        TextView pw_title =findViewById(R.id.txt_connect_password);
        pw_title.setText("상대방의 코드");

        btn.setText("코드 입력 완료");
        btn2.setText("연동하기");
    }
}
