package com.examplenasa.roma.mytestapp.application;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by valery on 05.11.16.
 */

public class MyTestApplication extends Application {

    @Override
    public void onCreate() {
        FacebookSdk.sdkInitialize(this);
    }
}
