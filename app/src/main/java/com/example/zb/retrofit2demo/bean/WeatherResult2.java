package com.example.zb.retrofit2demo.bean;

import com.google.gson.JsonObject;

/**
 * Created by zb on 2019/4/20.
 */

public class WeatherResult2 extends BaseResult {
    private int        code;
    private String     msg;
    private JsonObject data; // 数据部分也是一个bean，用JsonObject代替了

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

}
