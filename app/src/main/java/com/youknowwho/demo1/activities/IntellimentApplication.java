package com.youknowwho.demo1.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class IntellimentApplication extends Application {


    private Activity mActivity;
    private static IntellimentApplication instance;


    public static Context getContext() {
        return instance.getActivity();
    }


    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();

    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }


}
