package com.examplenasa.roma.mytestapp.api;

/**
 * Created by valery on 05.11.16.
 */

public class UserAccount {

    private String mUserName;
    private String mPassword;

    private String mToken;

    public UserAccount() {
    }


    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getTokken() {
        return mToken;
    }

    public void setToken(String mTokken) {
        this.mToken = mTokken;
    }
}
