package com.dobody.bkk.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.dobody.bkk.BaseActivity;
import com.dobody.bkk.R;

/**
 * A login screen that offers login via email/password.
 */
public class VerificationActivity extends BaseActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
    }
}

