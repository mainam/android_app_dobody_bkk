package com.dobody.bkk.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.dobody.bkk.BaseActivity;
import com.dobody.bkk.R;
import com.dobody.bkk.dataaccess.UserInfo;
import com.dobody.bkk.utils.ClientUtils;
import com.dobody.bkk.utils.ConvertUtils;
import com.google.gson.JsonObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * A login screen that offers login via email/password.
 */
public class SplashActivity extends BaseActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}

