package com.dobody.bkk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
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
import com.dobody.bkk.dataaccess.CountryInfo;
import com.dobody.bkk.dataaccess.UserInfo;
import com.dobody.bkk.utils.AndroidPermissionUtils;
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
    private String countryName= "";

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

        requestPermission(new AndroidPermissionUtils.OnCallbackRequestPermission() {
            @Override
            public void onSuccess() {
                try {
                    TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String mPhoneNumber = tMgr.getLine1Number();
                    if (mPhoneNumber != null)
                        txtMobile.setText(mPhoneNumber);
                    countryName = CountryInfo.getCountryName(tMgr.getSimCountryIso());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void currentPerrmission(boolean hasPermission) {
            }
        }, AndroidPermissionUtils.TypePermission.PERMISSION_READ_SMS);
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
                String mobile = txtMobile.getText().toString();
//                if(!mobile.startsWith("65"))
//                    mobile="65"+mobile;

                register(txtID.getText().toString(), txtUserName.getText().toString(), mobile, txtPassword.getText().toString(), txtConfirmPassword.getText().toString(),countryName);
                break;
        }
    }

    private void register(final String id, final String username, final String mobile, final String password, final String confirmPassword, final String country) {
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
                            JsonObject jsonObject1 = ConvertUtils.toJsonObject(jsonObject.get("data"));
                            jsonObject1 = ConvertUtils.toJsonObject(jsonObject1.get("user"));
                            UserInfo.EnumStatus status = UserInfo.EnumStatus.parse(ConvertUtils.toString(jsonObject1.get("status")));
                            switch (status) {
                                case SUSPENDED:
                                    LoginActivity.open(getActivity());
                                    break;
                                case ACTIVE:
                                    UserInfo.setCurrentUser(getActivity(), jsonObject);
                                    UserProfileActivity.open(getActivity(), id);
                                    break;
                                case PENDING:
                                    UserInfo.setCurrentUser(getActivity(), jsonObject);
                                    UserProfileActivity.open(getActivity(), id);
                                case VERIFYING:
                                    UserInfo.setCurrentUser(getActivity(), jsonObject);
                                    VerificationActivity.open(getActivity());
                                    break;

                            }
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
                    return UserInfo.register(id, username, mobile, password, confirmPassword,country);
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

