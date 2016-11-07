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
    Button linkedlnLogin;


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
        linkedlnLogin = (Button) findViewById(R.id.button_log_in_linkedin);
        linkedlnLogin.setOnClickListener(this);
    }

    Intent intent;

    @Override
    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
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
            case  R.id.button_log_in_linkedin:
                //////////
                break;
        }

    }
}
