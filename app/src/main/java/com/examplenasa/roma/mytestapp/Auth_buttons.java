package com.examplenasa.roma.mytestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Auth_buttons extends AppCompatActivity implements View.OnClickListener {
    Button googleLogin;
    Button twitterLogin;
    Button facebookLogin;
    Button googleSheets;


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_buttons);
        googleLogin = (Button) findViewById(R.id.button_log_in_google);
        googleLogin.setOnClickListener(this);
        twitterLogin = (Button) findViewById(R.id.button_log_in_twitter);
        twitterLogin.setOnClickListener(this);
        facebookLogin = (Button)findViewById(R.id.button_log_in_facebook);
        facebookLogin.setOnClickListener(this);
        googleSheets = (Button) findViewById(R.id.button_goole_sheets);
        googleSheets.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_log_in_google:
            intent = new Intent(this, Authorization_Google.class);
                  startActivity(intent);
             break;
            case R.id.button_log_in_facebook:
                intent = new Intent(this, Authorization_Facebook.class);
                    startActivity(intent);
                break;
            case  R.id.button_log_in_twitter:
                intent = new Intent(this, Authorization_Twitter.class);
                startActivity(intent);
                break;
            case  R.id.button_goole_sheets:
                intent = new Intent(this, SheetsGoogle.class);
                startActivity(intent);
                break;
            case R.id.button_sqlite:
                intent = new Intent(this, DbActivity.class);
                startActivity(intent);
                break;
        }

    }
}
