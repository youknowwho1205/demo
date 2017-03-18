package com.youknowwho.demo1.api.request;

import android.content.Context;

import com.google.gson.Gson;

public class BaseRequest {


    // Transient keyword is used to exclude mContext from Gson serialization/deserialization
    private transient Context mContext;

    public BaseRequest(Context context) {
        mContext = context;
    }


    public String toJson() {

        return new Gson().toJson(this);
    }
}