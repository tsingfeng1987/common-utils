package com.qing.common.utils.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


/**
 * Created by Guoqingfeng on 2017/10/10.
 */
public class JsonObj {

    private int anInt;

    private String anStr;

    @JsonProperty("an_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z")
    private Date anDate;

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public String getAnStr() {
        return anStr;
    }

    public void setAnStr(String anStr) {
        this.anStr = anStr;
    }

    public Date getAnDate() {
        return anDate;
    }

    public void setAnDate(Date anDate) {
        this.anDate = anDate;
    }
}
