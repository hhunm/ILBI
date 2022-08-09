package com.example.ilbi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseDatabase database;
    private ListView listView;
    private ArrayList<String> rc;
    private SharedPreferences preferences;


    final String TAG = "RecordActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);

        toolbarInit();


        //2022.08.24
        //리스트뷰
        listView = findViewById(R.id.list_record);
        rc = new ArrayList();


        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference reportRef = database.getReference("RECORD");

        reportRef.child("record").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    Log.d(TAG, String.valueOf(task.getResult().getValue()));

                    Map<String,Map> m = (Map) task.getResult().getValue();
                    Iterator<String> it = m.keySet().iterator();
                    while(it.hasNext()){
                        String key = it.next();
                        Log.d(TAG,"key: "+key);
                        Record r = new Record();
                        rc.add(r.mapToRecord(m.get(key)).toString());
                        Log.d(TAG,"map: "+m.get(key));

                    }
                    setView();
                }
            }
        });




    }
    private void setView(){
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        boolean isNormal = preferences.getBoolean("isNormal",true);
        ArrayAdapter<String> adapter;
        if(isNormal){
            adapter = new ArrayAdapter<String>(this,R.layout.list_item_normal,R.id.textView_normal, rc);
        }else{
            adapter = new ArrayAdapter<String>(this,R.layout.list_item_big,R.id.textView_big, rc);
        }

        //Log.d(TAG,rc.toString());
        listView.setAdapter(adapter);
    }

    private void toolbarInit(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("낙상 기록");
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
}
