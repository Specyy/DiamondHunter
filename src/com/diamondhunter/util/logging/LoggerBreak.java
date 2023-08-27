package com.diamondhunter.util.logging;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoggerBreak {
    private String key;
    private String value;

    public LoggerBreak(){
        key = null;
        value = null;
    }

    public LoggerBreak(String key, String br){
        this.key = key;
        this.value = br;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setBreak(String br) {
        this.value = br;
    }

    public String getKey() {
        return key;
    }

    public String getBreak() {
        return value;
    }

    protected Map<String, String> asMap(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    @Override
    public String toString() {
        return "[key=" + key + ";break=" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Iterable){
            Iterable it = (Iterable) obj;
            Iterator ite = it.iterator();

            int length = 0;

            boolean checkedKey = false;
            while(ite.hasNext()){
                length++;
                if(length > 2) return false;

                Object next = ite.next();

                if(key.equals(next.toString())){
                    checkedKey = true;
                    continue;
                }

                if(checkedKey && value.equals(next.toString())){
                    return true;
                }
            }
        } else if(obj instanceof String) {
            return key.equals(obj);
        } else if(obj instanceof LoggerBreak){
            LoggerBreak br = (LoggerBreak) obj;

            return key.equals(br.key) && value.equals(br.value);
        }

        return false;
    }
}
