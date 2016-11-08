package com.examplenasa.roma.mytestapp.application;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.TwitterAuthCredential;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public class MyTestApplication extends Application {


    @Override
    public void onCreate() {
        FacebookSdk.sdkInitialize(this);


    }
}
