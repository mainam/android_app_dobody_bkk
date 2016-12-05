package com.dobody.bkk.dataaccess;

import android.content.Context;
import android.util.Log;

import com.dobody.bkk.utils.ConvertUtils;
import com.dobody.bkk.utils.SharedPreferencesUtil;

/**
 * Created by MaiNam on 8/6/2016.
 */

public class DataCacheInfo {

    private static final String TAG = DataCacheInfo.class.getSimpleName();

    public enum EnumCacheType {
        CurrentUser
    }

    public static String getData(Context context, EnumCacheType enumCacheType, String key) {
        try {
            Log.d(TAG, "getData: " + enumCacheType.toString() + "_" + key);
            String data = SharedPreferencesUtil.getString(context, enumCacheType.toString() + "_" + key, "");
            Log.d(TAG, "getData: " + data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setData(Context context, EnumCacheType enumCacheType, String key, Object data) {
        try {
            SharedPreferencesUtil.setSharedPreferences(context, SharedPreferencesUtil.EnumType.String, enumCacheType.toString() + "_" + key, ConvertUtils.toJson(data));
            Log.d(TAG, "setData: " + enumCacheType.toString() + "_" + key);
            Log.d(TAG, "setData: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}