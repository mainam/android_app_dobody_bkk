package com.dobody.bkk;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dobody.bkk.constant.Constants;
import com.dobody.bkk.utils.AndroidPermissionUtils;

/**
 * Created by Admin on 12/1/2016.
 */

public class BaseActivity extends AppCompatActivity {
    private Handler mHandler;
    private static final int PUSH_MAIN_THREAD = 10004;
    public static final int TIMEOUT_REQUEST_EXCEPTION = 10001;
    public static final int CHECK_CONNECTIVITY = 10003;
    public static final int SHOW_SNACKBAR_MESSAGE = 10002;

    public void pullDataFromHandler(Message msg) {

    }

    public interface ReloadListener {
        void reload();

    }
    ReloadListener reload;

    public void pushToMainThread(Object objects, Bundle dataBundle) {
        try {

            Message message = new Message();
            message.what = PUSH_MAIN_THREAD;
            message.obj = objects;
            message.setData(dataBundle);
            getHandler().sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showSnackBar(Context context, String msg) {
        try {
            if (context instanceof BaseActivity)
                ((BaseActivity) context).showSnackBar(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSnackBar(Context context, @StringRes int resourceId) {
        try {
            String string = (context.getResources().getString(resourceId));
            showSnackBar(context, string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSnackBar(Context context, String msg, String callAgainName, ReloadListener reloadListener) {
        try {
            if (context instanceof BaseActivity)
                ((BaseActivity) context).showSnackBar(msg, callAgainName, reloadListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void showSnackBar(View v, String msg) {
        try {
            Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void showSnackBar(@IdRes int id, String msg) {
        showSnackBar(findViewById(id), msg);
    }

    public synchronized void showSnackBar(String msg, String callAgainName, ReloadListener reloadListener) {
        this.reload = reloadListener;
        Message message = new Message();
        message.what = SHOW_SNACKBAR_MESSAGE;
        if (msg == null)
            msg = "";
        message.getData().putString(Constants.SNACKBAR_MESSAGE, msg);
        if (callAgainName != null)
            message.getData().putString(Constants.SNACKBAR_BUTTON_RELOAD_NAME, callAgainName);
        getHandler().sendMessage(message);
    }

    public synchronized void showSnackBar(CharSequence msg) {
        showSnackBar(msg == null ? "" : msg.toString());
    }

    public synchronized void showSnackBar(String msg) {
        this.reload = null;
        Message message = new Message();
        message.what = SHOW_SNACKBAR_MESSAGE;
        if (msg == null)
            msg = "";
        message.getData().putString(Constants.SNACKBAR_MESSAGE, msg);
        getHandler().sendMessage(message);
    }

    public synchronized void showSnackBar(@StringRes int resourceId) {
        try {
            String string = getResources().getString(resourceId);
            showSnackBar(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void showTimeOutError(ReloadListener reloadListener) {
        this.reload = reloadListener;
        Message message = new Message();
        message.what = TIMEOUT_REQUEST_EXCEPTION;
        message.obj = reloadListener;
        getHandler().sendMessage(message);
    }


    public static void showTimeOutError(Context context) {
        try {
            if (context instanceof BaseActivity)
                ((BaseActivity) context).showTimeOutError();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTimeOutError(Context context, ReloadListener reloadListener) {
        try {
            if (context instanceof BaseActivity)
                ((BaseActivity) context).showTimeOutError(reloadListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void showTimeOutError() {
        this.reload = null;
        Message message = new Message();
        message.what = TIMEOUT_REQUEST_EXCEPTION;
        getHandler().sendMessage(message);
    }


    public synchronized void showCheckConnection(ReloadListener reloadListener) {
        this.reload = reloadListener;
        Message message = new Message();
        message.what = CHECK_CONNECTIVITY;
        message.obj = reloadListener;
        getHandler().sendMessage(message);
    }


    public static void showCheckConnection(Context context) {
        try {
            if (context instanceof BaseActivity)
                ((BaseActivity) context).showCheckConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCheckConnection(Context context, ReloadListener reloadListener) {
        try {
            if (context instanceof BaseActivity)
                ((BaseActivity) context).showCheckConnection(reloadListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void showCheckConnection() {
        this.reload = null;
        Message message = new Message();
        message.what = CHECK_CONNECTIVITY;
        getHandler().sendMessage(message);
    }


    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(final Message msg) {
                    switch (msg.what) {
                        case PUSH_MAIN_THREAD:
                            pullDataFromHandler(msg);
                            break;
                        case CHECK_CONNECTIVITY:
                            try {

                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.common_msg_check_connectivity, Snackbar.LENGTH_LONG);
                                if (reload != null) {
                                    snackbar.setAction(R.string.common_msg_retry, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                reload.reload();
                                                reload = null;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).setActionTextColor(Color.RED);
                                }
                                snackbar.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        case TIMEOUT_REQUEST_EXCEPTION:
                            try {

                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.common_msg_connection_error, Snackbar.LENGTH_LONG);
                                if (reload != null) {
                                    snackbar.setAction(R.string.common_msg_retry, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                reload.reload();
                                                reload = null;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).setActionTextColor(Color.RED);
                                }
                                snackbar.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        case SHOW_SNACKBAR_MESSAGE:
                            try {

                                String mes = msg.getData().getString(Constants.SNACKBAR_MESSAGE);
                                if (mes == null) mes = "";
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mes, Snackbar.LENGTH_LONG);
                                String buttonName = msg.getData().getString(Constants.SNACKBAR_BUTTON_RELOAD_NAME);
                                if (buttonName == null) buttonName = "";
                                if (reload != null && !buttonName.isEmpty()) {
                                    snackbar.setAction(buttonName, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                reload.reload();
                                                reload = null;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).setActionTextColor(Color.RED);
                                }
                                snackbar.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                    }
                    super.handleMessage(msg);
                }
            };
        }
        return mHandler;
    }
    public AppCompatActivity getActivity() {
        return this;
    }

    public BaseActivity getBaseActivity() {
        return this;
    }

    public AndroidPermissionUtils.OnCallbackRequestPermission callbackRequestPermission = null;

    public void requestPermission(AndroidPermissionUtils.OnCallbackRequestPermission onCallbackRequestPermission, AndroidPermissionUtils.TypePermission... typePermissions) {
        callbackRequestPermission = onCallbackRequestPermission;
        boolean hasPermission = AndroidPermissionUtils.mayRequestPermission(getActivity(), typePermissions);
        if (callbackRequestPermission != null) {
            callbackRequestPermission.currentPerrmission(hasPermission);
        }
    }

    public static void requestPermission(Context context, AndroidPermissionUtils.OnCallbackRequestPermission onCallbackRequestPermission, AndroidPermissionUtils.TypePermission... typePermissions) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).requestPermission(onCallbackRequestPermission, typePermissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndroidPermissionUtils.getPermission(requestCode, grantResults, callbackRequestPermission);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
