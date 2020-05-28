package com.granny.guide2;

import android.app.Application;

import com.ijvpbsdiwc.adx.service.AdsExchange;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AdsExchange.init(this, "");
    }
}
