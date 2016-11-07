package com.examplenasa.roma.mytestapp;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = new Intent(this, Authorization.class);
        startActivity(intent);


    }
}
