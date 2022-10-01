package com.example.ilbi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class User {
    private final String TAG = "User";
    private static User user;
    private FirebaseDatabase database;

    private String senior_name;
    private String senior_number;
    private String senior_address;
    private String my_id;
    private String my_password;
    private String ip;
    private String protector_name;
    private String protector_number;
    private Boolean my_role;
    private String partner_id;
    private Boolean isTokenChecked = false;

    private User(){ }
    public static User getInstance(){
        if(user == null){
            user = new User();
        }
        return user;
    }

    public void signUP(){
        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference idRef = database.getReference("ID_LIST");
        idRef.child(my_id).setValue(my_password);

        DatabaseReference myRef = database.getReference("USER");
        myRef.child(my_id).child("INFO").child("Password").setValue(my_password);
        myRef.child(my_id).child("INFO").child("isReport").setValue("INACTIVATED");
        if(my_role){
            myRef.child(my_id).child("INFO").child("Name").setValue(senior_name);
            myRef.child(my_id).child("INFO").child("Number").setValue(senior_number);
            myRef.child(my_id).child("INFO").child("Address").setValue(senior_address);
            myRef.child(my_id).child("INFO").child("IP").setValue(ip);
            myRef.child(my_id).child("INFO").child("Role").setValue("SENIOR");
            myRef.child(my_id).child("INFO").child("Camera").setValue("OFF");
        }else{
            myRef.child(my_id).child("INFO").child("Name").setValue(protector_name);
            myRef.child(my_id).child("INFO").child("Number").setValue(protector_number);
            myRef.child(my_id).child("INFO").child("Role").setValue("PROTECTOR");
        }

    }

    public void checkIdDuplicated(String id, Context context, Intent intent){
        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference idRef = database.getReference("ID_LIST");
        idRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String,Map> m = (Map) task.getResult().getValue();

                Iterator<String> it = m.keySet().iterator();
                while(it.hasNext()){
                    String key = it.next();
                    //Log.d(TAG,"key: " + key);
                    if(id.equals(key)){
                        Toast.makeText(context,"중복되는 아이디입니다.",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"ID 중복");
                        return;
                    }
                }
                Log.d(TAG,"사용가능한 ID");
                context.startActivity(intent);
            }
        });

    }

    public void login(String id, String pw, Context context){
        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("ID_LIST");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String,Map> m = (Map) task.getResult().getValue();

                Iterator<String> it = m.keySet().iterator();
                while(it.hasNext()){
                    String key = it.next();
                    //Log.d(TAG,"key: " + key);
                    if(id.equals(key)){
                        if(pw.equals(m.get(key))){
                            Log.d(TAG,"login success");
                            my_id = id;
                            userInit(context, new Intent(context, MainActivity.class));
                            //isLogin = true;
                            return;
                        }else{
                            Log.d(TAG, "login fail: pw");
                            Toast.makeText(context,"로그인 실패",Toast.LENGTH_SHORT).show();
                            //isLogin = false;
                        }
                    }
                }
                Toast.makeText(context,"로그인 실패",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "login fail:no id");
            }
        });
    }

    public void userInit(Context context, Intent intent){
        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("USER");

        userRef.child(my_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
                    Map<String,Map> m = (Map) task.getResult().getValue();
                    Map<String, Object> info = m.get("INFO");
                    partner_id = String.valueOf(info.get("Partner_ID"));

                    SharedPreferences preferences = context.getSharedPreferences("UserInfo", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("my_id", my_id);
                    editor.putBoolean("isLogin", true);
                    switch (info.get("Role").toString()){
                        case "SENIOR":
                            my_role = true;
                            editor.putString("senior_id", my_id);
                            editor.putString("role", "Senior");
                            seniorInit(info);
                            partnerInit(context, intent);
                            Log.d(TAG, "Shared Preference | role: " + preferences.getString("role","fail"));
                            Log.d(TAG, "Shared Preference | senior_id: " + preferences.getString("senior_id","fail"));
                            break;
                        case "PROTECTOR":
                            my_role = false;
                            editor.putString("role", "Protector");
                            editor.putString("senior_id", partner_id);
                            protectorInit(info);
                            partnerInit(context, intent);
                            break;
                    }
                    editor.commit();
                }
            }
        });

    }

    public void partnerInit(Context context, Intent intent) {
        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("USER");

        userRef.child(partner_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
                    Map<String,Map> m = (Map) task.getResult().getValue();
                    Map<String, Object> info = m.get("INFO");

                    switch (info.get("Role").toString()){
                        case "SENIOR":
                            seniorInit(info);
                            Log.d(TAG, "check: \n" + getString());
                            break;
                        case "PROTECTOR":
                            protectorInit(info);
                            Log.d(TAG, "check: \n" + getString());
                            break;
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);

                }
            }
        });
    }

    private void seniorInit(Map<String, Object> map){
        this.senior_name = String.valueOf(map.get("Name"));
        this.senior_number = String.valueOf(map.get("Number"));
        this.senior_address= String.valueOf(map.get("Address"));
        this.ip= String.valueOf(map.get("IP"));

    }

    private void protectorInit(Map<String, Object> map){
        this.protector_name = String.valueOf(map.get("Name"));
        this.protector_number = String.valueOf(map.get("Number"));
    }

    public String makeToken(){
        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("TOKEN_LIST");
        Random random = new Random();
        String token = String.valueOf(random.nextInt(1000000));
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(token, my_id);
        myRef.updateChildren(childUpdates);
        return token;
    }

    public boolean checkToken(String token){
        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("TOKEN_LIST");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, String> m = (Map) task.getResult().getValue();

                Iterator<String> it = m.keySet().iterator();
                while(it.hasNext()){
                    String key = it.next();
                    Log.d(TAG,"key: " + key);
                    if(token.equals(key)){
                        partner_id = m.get(key);
                        Log.d(TAG,"partner_id: " +  m.get(key));
                        DatabaseReference myRef = database.getReference("USER");
                        myRef.child(my_id).child("INFO").child("Partner_ID").setValue(partner_id);
                        isTokenChecked = true;
                        break;
                    }
                }
            }
        });
        return true;
    }

    public void checkPartner(Context context){
        database = FirebaseDatabase.getInstance("https://test-8bbfd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference partnerRef = database.getReference("USER");
        Log.d(TAG, "check partner_id: "+partner_id);

            partnerRef.child(partner_id).child("INFO").child("Partner_ID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Error getting data", task.getException());
                    } else {
                        String p_id = task.getResult().getValue().toString();
                        Log.d(TAG, "is null: " + p_id);
                        if(p_id == null){
                            return;
                        }

                        if(p_id.equals(my_id)){
                            Log.d(TAG, "connect success");
                            Toast.makeText(context,"회원가입 성공",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            return;
                        }else{
                            Log.d(TAG, "connect fail");
                            Toast.makeText(context,"회원가입 실패",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });


    }

    public String getString(){
        StringBuffer sb = new StringBuffer();
        sb.append("senior_name: " + this.senior_name + "\n");
        sb.append("senior_number: " + this.senior_number + "\n");
        sb.append("senior_address: " + this.senior_address + "\n");
        sb.append("my_id: " + this.my_id + "\n");
        sb.append("my_password: " + this.my_password + "\n");
        sb.append("ip: " + this.ip + "\n");
        sb.append("protector_name: " + this.protector_name + "\n");
        sb.append("protector_number: " + this.protector_number + "\n");
        sb.append("my_role: " + this.my_role + "\n");
        sb.append("partner_id: " + this.partner_id + "\n");
         return sb.toString();
    }

    public Boolean getTokenChecked() {
        return isTokenChecked;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public String getSenior_name() {
        return senior_name;
    }

    public void setSenior_name(String senior_name) {
        this.senior_name = senior_name;
    }

    public String getSenior_number() {
        return senior_number;
    }

    public void setSenior_number(String senior_number) {
        this.senior_number = senior_number;
    }

    public String getSenior_address() {
        return senior_address;
    }

    public void setSenior_address(String senior_address) {
        this.senior_address = senior_address;
    }

    public String getMy_id() {
        return my_id;
    }

    public void setMy_id(String senior_id) {
        this.my_id = senior_id;
    }

    public String getMy_password() {
        return my_password;
    }

    public void setMy_password(String senior_password) {
        this.my_password = senior_password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProtector_name() {
        return protector_name;
    }

    public void setProtector_name(String protector_name) {
        this.protector_name = protector_name;
    }

    public String getProtector_number() {
        return protector_number;
    }

    public void setProtector_number(String protector_number) {
        this.protector_number = protector_number;
    }

    public Boolean getMy_role() {
        return my_role;
    }

    public void setMy_role(Boolean my_role) {
        this.my_role = my_role;
    }
}
