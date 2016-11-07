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



    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Z4uq5htTrrQjVeac3Y3IzmN55";
    private static final String TWITTER_SECRET = "v5tnRVggYbZFpX7Ofug2ZFCeDmFcnfLXkmD4QgTDnY90DILQFc";


    @Override
    public void onCreate() {
        FacebookSdk.sdkInitialize(this);


     //   PsiMethod:onCreateTwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
   //  PsiMethod:onCreateFabric.with(this, new Twitter(authConfig));ig));




    }
}
