package com.dobody.bkk.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.dobody.bkk.constant.Constants;

import java.util.ArrayList;

/**
 * Created by Admin on 6/23/2016.
 */
public class AndroidPermissionUtils {
    private static final int REQUEST_READ_CONTACTS = 100;

    public enum TypePermission {
        PERMISSION_READ_SMS(Manifest.permission.READ_SMS);

        String permission = "";

        TypePermission(String permission) {
            this.permission = permission;
        }

        public String getValue() {
            return permission;
        }
    }

    public static boolean mayRequestPermission(final Context activity, final TypePermission... typePermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (activity == null)
            return false;

        ArrayList<String> needRequests = new ArrayList<>();
        for (TypePermission typePermission : typePermissions) {
            if (activity.checkSelfPermission(typePermission.getValue()) != PackageManager.PERMISSION_GRANTED) {
                if (activity instanceof Activity && ((Activity) activity).shouldShowRequestPermissionRationale(typePermission.getValue())) {
                    needRequests.add(typePermission.getValue());
                } else {
                    needRequests.add(typePermission.getValue());
                }
            }
        }
        if (needRequests.size() > 0) {
            String[] stockArr = new String[needRequests.size()];
            stockArr = needRequests.toArray(stockArr);
            if (activity instanceof Activity)
                ((Activity) activity).requestPermissions(stockArr, Constants.REQ_REQUEST_PERMISSION);
        } else
            return true;

        return false;
    }

    public static boolean hasPermission(final Activity activity, final TypePermission... typePermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (activity == null)
            return false;

        ArrayList<String> needRequests = new ArrayList<>();
        for (TypePermission typePermission : typePermissions) {
            if (activity.checkSelfPermission(typePermission.getValue()) != PackageManager.PERMISSION_GRANTED) {
                if (activity.shouldShowRequestPermissionRationale(typePermission.getValue())) {
                    needRequests.add(typePermission.getValue());
                } else {
                    needRequests.add(typePermission.getValue());
                }
            }
        }
        return needRequests.size() == 0;
    }

    public interface OnCallbackRequestPermission {
        void onSuccess();

        void onFailed();

        void currentPerrmission(boolean hasPermission);
    }

    public static void getPermission(int requestCode, int[] grantResults, OnCallbackRequestPermission callBack) {
        if (requestCode == Constants.REQ_REQUEST_PERMISSION)
            if (callBack != null) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callBack.onSuccess();
                else
                    callBack.onFailed();
            }
    }
}
