package com.dobody.bkk.dataaccess;

import android.content.Context;

import com.dobody.bkk.constant.ServerConstants;
import com.dobody.bkk.constant.TimeoutConstants;
import com.dobody.bkk.utils.ClientUtils;
import com.dobody.bkk.utils.ConvertUtils;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by MaiNam on 11/30/2016.
 */

public class UserInfo {
    private static final String TAG = UserInfo.class.getSimpleName();
    static JsonObject currentUser;

    public static JsonObject getCurrentUser() {
        return currentUser;
    }

    public synchronized static void setCurrentUser(Context mContext, JsonObject currentUser) {
        UserInfo.currentUser = currentUser;
        try {
            if (currentUser != null) {
                DataCacheInfo.setData(mContext, DataCacheInfo.EnumCacheType.CurrentUser, "", currentUser);
            } else {
                DataCacheInfo.setData(mContext, DataCacheInfo.EnumCacheType.CurrentUser, "", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ClientUtils.DataResponse login(String username, String password) throws SocketTimeoutException, UnknownHostException {
        try {
            return ClientUtils.postData(ServerConstants.API_LOGIN, new JSONObject().put("nric", username).put("password", password), TimeoutConstants.TIMEOUT_DEFAULT);
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (UnknownHostException e) {
            throw e;
        } catch (Exception e) {
            return new ClientUtils.DataResponse(ServerConstants.RESPONSE_ERROR);

        }
    }

    public static ClientUtils.DataResponse register(String id, String username, String mobile, String password) throws UnknownHostException, SocketTimeoutException {
        try {
            return ClientUtils.postData(ServerConstants.API_LOGIN, new JSONObject()
                            .put("nric", username)
                            .put("password", password)
                            .put("name", "test name")
                            .put("phone", mobile)
                            .put("email", "test@gmail.com")
                            .put("dob", "12/11/2016")
                            .put("gender", "nam")
                            .put("passport", id)
                            .put("nationality", "vietnam")
                            .put("address", "tanh linh binh thuan")
                            .put("loyalty", 1)
                            .put("country", "vn")
                            .put("status", 1)
                    , TimeoutConstants.TIMEOUT_DEFAULT);
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (UnknownHostException e) {
            throw e;
        } catch (Exception e) {
            return new ClientUtils.DataResponse(ServerConstants.RESPONSE_ERROR);

        }
    }

    public static ClientUtils.DataResponse verify(String username, String token, String otp) throws SocketTimeoutException, UnknownHostException {
        try {
            return ClientUtils.postData(ServerConstants.API_LOGIN, new JSONObject()
                            .put("nric", username)
                            .put("token", token)
                            .put("name", "test name")
                            .put("otp", otp)
                    , TimeoutConstants.TIMEOUT_DEFAULT);
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (UnknownHostException e) {
            throw e;
        } catch (Exception e) {
            return new ClientUtils.DataResponse(ServerConstants.RESPONSE_ERROR);

        }
    }

    public static boolean isLogin() {
        return currentUser != null;
    }

    public static String getUsername() {
        if (!isLogin())
            return "";
        JsonObject jsonObject = ConvertUtils.toJsonObject(currentUser.get("data"));
        return ConvertUtils.toString(jsonObject.get("username"));
    }

    public static String getToken() {
        if (!isLogin())
            return "";
        JsonObject jsonObject = ConvertUtils.toJsonObject(currentUser.get("data"));
        return ConvertUtils.toString(jsonObject.get("access_token"));
    }
}
