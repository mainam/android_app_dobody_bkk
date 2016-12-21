package com.dobody.bkk.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class VerificationActivity extends BaseActivity implements View.OnClickListener {
    private View btnVerify;
    private View ivLoading;
    private View ivDone;
    private TextView tvErrorMessage;
    private EditText txtCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.btnResend).setOnClickListener(this);
        btnVerify = findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(this);
        ivLoading = findViewById(R.id.ivProcessLoading);
        ivDone = findViewById(R.id.ivDone);
        tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        txtCode = (EditText) findViewById(R.id.txtVerificationCode);

    }

    public static void open(AppCompatActivity activity) {
        activity.startActivity(new Intent(activity, VerificationActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnResend:
                if (currentTime > new Date().getTime() - 900000 && time >= 3)
                    break;

                View v = getLayoutInflater().inflate(R.layout.dialog_resend_verification_code, null, false);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(v).create();
                alertDialog.show();
                v.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                v.findViewById(R.id.btnResend).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resend();
                        alertDialog.dismiss();
                    }
                });
                break;
            case R.id.btnVerify:
                verify(txtCode.getText().toString());
                break;
        }
    }


    private void verify(final String code) {

        new AsyncTask<Void, Void, ClientUtils.DataResponse>() {
            @Override
            protected void onPreExecute() {
                btnVerify.setEnabled(false);
                ivLoading.setVisibility(View.VISIBLE);
                ivDone.setVisibility(View.GONE);
                tvErrorMessage.setVisibility(View.GONE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final ClientUtils.DataResponse aVoid) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (aVoid != null && aVoid.is200()) {
                            btnVerify.setEnabled(true);
                            ivLoading.setVisibility(View.GONE);
                            ivDone.setVisibility(View.GONE);
                            MainActivity.open(getActivity());
                        } else {
                            JsonObject jsonObject = ConvertUtils.toJsonObject(aVoid.getBody());
                            jsonObject = ConvertUtils.toJsonObject(jsonObject.get("data"));
                            String data = ConvertUtils.toString(jsonObject.get("otp"));
                            tvErrorMessage.setText(data);
                            tvErrorMessage.setVisibility(View.VISIBLE);
                            if (data.isEmpty()) {
                                data = ConvertUtils.toString(jsonObject.get("error"));
                                tvErrorMessage.setText(data);

                            }
                            btnVerify.setEnabled(true);
                            ivLoading.setVisibility(View.GONE);
                        }
                    }
                }, 1000);
                super.onPostExecute(aVoid);

            }

            @Override
            protected ClientUtils.DataResponse doInBackground(Void... voids) {
                try {
                    return UserInfo.verify(UserInfo.getToken(), code);
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    long currentTime = 0;
    int time = 0;

    private void resend() {

        currentTime = new Date().getTime();
        time++;
        new AsyncTask<Void, Void, ClientUtils.DataResponse>() {
            @Override
            protected void onPreExecute() {
                btnVerify.setEnabled(false);
                ivLoading.setVisibility(View.VISIBLE);
                ivDone.setVisibility(View.GONE);
                tvErrorMessage.setVisibility(View.GONE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final ClientUtils.DataResponse aVoid) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (aVoid != null && aVoid.is200()) {
                            btnVerify.setEnabled(true);
                            ivLoading.setVisibility(View.GONE);
                            ivDone.setVisibility(View.VISIBLE);
                        } else {
                            JsonObject jsonObject = ConvertUtils.toJsonObject(aVoid.getBody());
                            tvErrorMessage.setText(ConvertUtils.toString(jsonObject.get("message")));
                            tvErrorMessage.setVisibility(View.VISIBLE);
                        }
                        btnVerify.setEnabled(true);
                        ivLoading.setVisibility(View.GONE);
                    }
                }, 1000);
                super.onPostExecute(aVoid);

            }

            @Override
            protected ClientUtils.DataResponse doInBackground(Void... voids) {
                try {
                    return UserInfo.resend(UserInfo.getToken());
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

