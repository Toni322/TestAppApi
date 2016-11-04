package com.examplenasa.roma.mytestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

SignInButton signInButton;
    Button signtOutButton;
    TextView statusTextView;
    GoogleApiClient mGoogleApiClient;
    public  static final String TAG = "SignActivity";
    public  static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        statusTextView = (TextView) findViewById(R.id.status_text);
        signInButton = (SignInButton) findViewById(R.id.sing_in_button);
        signInButton.setOnClickListener(this);

        signtOutButton = (Button) findViewById(R.id.logout_button);
        signtOutButton.setOnClickListener(this);
    }
    private void signIn(){
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent,RC_SIGN_IN);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sing_in_button;
            signIn();
             break;
         case R.id.logout_button;

             break;
        }
    }
}
