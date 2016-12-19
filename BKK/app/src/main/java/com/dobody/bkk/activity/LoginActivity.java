package com.dobody.bkk.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
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
public class LoginActivity extends BaseActivity implements OnClickListener {

    // UI references.
    private EditText txtUserName;
    private EditText txtPassword;
    private View btnLogin;
    private View ivLoading;
    private TextView tvErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set up the login form.
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        ivLoading = findViewById(R.id.ivProcessLoading);
        tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        findViewById(R.id.btnRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                login(txtUserName.getText().toString(), txtPassword.getText().toString());
                break;
            case R.id.btnRegister:
                RegisterActivity.open(getActivity());
                finish();
                break;
        }
    }


    private void login(final String username, final String password) {
        new AsyncTask<Void, Void, ClientUtils.DataResponse>() {
            @Override
            protected void onPreExecute() {
                btnLogin.setEnabled(false);
                ivLoading.setVisibility(View.VISIBLE);
                tvErrorMessage.setVisibility(View.GONE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final ClientUtils.DataResponse aVoid) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (aVoid != null && aVoid.is200()) {
                            JsonObject jsonObject = ConvertUtils.toJsonObject(aVoid.getBody());
                            JsonObject jsonObject1 = ConvertUtils.toJsonObject(jsonObject.get("data"));
                            jsonObject1.addProperty("username", username);
                            UserInfo.setCurrentUser(getActivity(), jsonObject);
                            UserProfileActivity.open(getActivity(),"");
                            finish();
                        } else {
                            JsonObject jsonObject = ConvertUtils.toJsonObject(aVoid.getBody());
                            tvErrorMessage.setText(ConvertUtils.toString(jsonObject.get("message")));
                            tvErrorMessage.setVisibility(View.VISIBLE);
                        }
                        btnLogin.setEnabled(true);
                        ivLoading.setVisibility(View.GONE);
                    }
                }, 1000);
                super.onPostExecute(aVoid);

            }

            @Override
            protected ClientUtils.DataResponse doInBackground(Void... voids) {
                try {
                    return UserInfo.login(username, password);
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public static void open(AppCompatActivity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
}

