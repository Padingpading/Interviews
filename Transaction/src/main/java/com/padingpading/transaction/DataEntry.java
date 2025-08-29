package com.padingpading.transaction;

import com.alibaba.excel.annotation.ExcelIgnore;

import java.util.Date;

public class DataEntry {
    private String time;
    @ExcelIgnore
    private Date date;
    private int count;
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
}

