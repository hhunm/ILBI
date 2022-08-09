package com.example.ilbi;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class Record {
    private String date_time;
    private boolean isReported;
    private String byWho;

    public Record(String date_time,  boolean isReported, String byWho){
        this.date_time = date_time;
        this.isReported = isReported;
        this.byWho = byWho;
    }
    public Record(){};

    public Map<String, Object> recordToMap(){
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("DateTime",date_time);
        hm.put("isReported",isReported);
        hm.put("byWho",byWho);
        return hm;
    }
    public Record mapToRecord(Map<String, Object> map){
        this.date_time = String.valueOf(map.get("DateTime"));
        this.isReported = (Boolean) map.get("isReported");
        this.byWho = String.valueOf(map.get("byWho"));
        return this;
    }

    public String toString(){
        String result = date_time+ " 낙상 사고를 감지했습니다. \n";
        if(isReported){
            switch(byWho){
                case "Senior":
                    result += "피보호자에 의해 신고되었습니다.";
                    break;
                case "Protector":
                    result += "보호자에 의해 신고되었습니다.";
                    break;
                default:
                    result += "시간초과로 신고되었습니다.";
            }
        }else{
            switch(byWho) {
                case "Senior":
                    result += "피보호자에 의해 신고가 취소되었습니다.";
                    break;
                case "Protector":
                    result += "보호자에 의해 신고가 취소되었습니다.";
                    break;
            }
        }
        return result;
    }

}
