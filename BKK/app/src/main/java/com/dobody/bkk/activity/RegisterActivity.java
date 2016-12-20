package com.dobody.bkk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
public class RegisterActivity extends BaseActivity implements OnClickListener {
    private EditText txtID;
    private EditText txtMobile;
    private EditText txtUserName;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private View btnRegister;
    private View ivLoading;
    private TextView tvErrorMessage;
    private TextInputLayout tlConfirmPassword;
    private TextInputLayout tlPassword;
    private TextInputLayout tlMobile;
    private TextInputLayout tlUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtID = (EditText) findViewById(R.id.txtId);
        txtMobile = (EditText) findViewById(R.id.txtMobile);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        ivLoading = findViewById(R.id.ivProcessLoading);
        tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        txtConfirmPassword.addTextChangedListener(textWatcher);
        tlConfirmPassword = (TextInputLayout) findViewById(R.id.tlConfirmPassword);
        tlPassword = (TextInputLayout) findViewById(R.id.tlPassword);
        tlMobile = (TextInputLayout) findViewById(R.id.tlMobile);
        tlUserName = (TextInputLayout) findViewById(R.id.tlUserName);
        txtPassword.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                tlConfirmPassword.setError(null);
            } else {
                tlConfirmPassword.setError(getResources().getString(R.string.common_unmatched_password));
                tlConfirmPassword.setErrorEnabled(true);
            }
        }
    };


    public static void open(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                LoginActivity.open(getActivity());
                finish();
                break;
            case R.id.btnRegister:
                tlMobile.setError(null);
                tlPassword.setError(null);
                tlConfirmPassword.setError(null);
                tlUserName.setError(null);
                if (!txtConfirmPassword.getText().toString().equals(txtPassword.getText().toString())) {
                    tlConfirmPassword.setError(getResources().getString(R.string.common_unmatched_password));
                    tlConfirmPassword.setErrorEnabled(true);
                    return;
                }
                register(txtID.getText().toString(), txtUserName.getText().toString(), txtMobile.getText().toString(), txtPassword.getText().toString(), txtConfirmPassword.getText().toString());
                break;
        }
    }

    private void register(final String id, final String username, final String mobile, final String password, final String confirmPassword) {
        new AsyncTask<Void, Void, ClientUtils.DataResponse>() {
            @Override
            protected void onPreExecute() {
                btnRegister.setEnabled(false);
                ivLoading.setVisibility(View.VISIBLE);
                tvErrorMessage.setVisibility(View.GONE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final ClientUtils.DataResponse aVoid) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (aVoid != null && aVoid.is201()) {
                            JsonObject jsonObject = ConvertUtils.toJsonObject(aVoid.getBody());
                            UserInfo.setCurrentUser(getActivity(), jsonObject);
                            UserProfileActivity.open(getActivity(), id, jsonObject.toString());
                            finish();
                        } else {
                            JsonObject jsonObject = ConvertUtils.toJsonObject(aVoid.getBody());
                            jsonObject = ConvertUtils.toJsonObject(jsonObject.get("data"));
                            if (jsonObject.has("username")) {
                                tlUserName.setError(ConvertUtils.toString(jsonObject.get("username")));
                                tlUserName.setErrorEnabled(true);
                            }
                            if (jsonObject.has("password")) {
                                tlPassword.setError(ConvertUtils.toString(jsonObject.get("password")));
                                tlPassword.setErrorEnabled(true);
                            }
                            if (jsonObject.has("password_confirm")) {
                                tlConfirmPassword.setError(ConvertUtils.toString(jsonObject.get("password_confirm")));
                                tlConfirmPassword.setErrorEnabled(true);
                            }
                            if (jsonObject.has("mobile")) {
                                tlMobile.setError(ConvertUtils.toString(jsonObject.get("mobile")));
                                tlMobile.setErrorEnabled(true);
                            }
                        }
                        btnRegister.setEnabled(true);
                        ivLoading.setVisibility(View.GONE);
                    }
                }, 1000);
                super.onPostExecute(aVoid);

            }

            @Override
            protected ClientUtils.DataResponse doInBackground(Void... voids) {
                try {
                    return UserInfo.register(id, username, mobile, password, confirmPassword);
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}

