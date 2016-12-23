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
            return ClientUtils.postData(ServerConstants.API_LOGIN, new JSONObject().put("username", username).put("password", password), TimeoutConstants.TIMEOUT_DEFAULT);
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (UnknownHostException e) {
            throw e;
        } catch (Exception e) {
            return new ClientUtils.DataResponse(ServerConstants.RESPONSE_ERROR);

        }
    }

    public static ClientUtils.DataResponse register(String id, String username, String mobile, String password, String passwordConfirm,String country) throws UnknownHostException, SocketTimeoutException {
        try {
            return ClientUtils.postData(ServerConstants.API_REGISTER, new JSONObject()
                            .put("document_id", id)
                            .put("mobile", mobile)
                            .put("username", username)
                            .put("password", password)
                            .put("password_confirm", passwordConfirm)
                            .put("country", country)
                            .put("agent", "BKK FOREX PTE LTD")
                    , TimeoutConstants.TIMEOUT_DEFAULT);
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (UnknownHostException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return new ClientUtils.DataResponse(ServerConstants.RESPONSE_ERROR);

        }
    }

    public static ClientUtils.DataResponse verify(String token, String otp) throws SocketTimeoutException, UnknownHostException {
        try {
            return ClientUtils.postData(ServerConstants.API_VERIFY, token, new JSONObject()
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

    public static ClientUtils.DataResponse resend(String token) throws SocketTimeoutException, UnknownHostException {
        try {
            return ClientUtils.postData(ServerConstants.API_RESEND, token, new JSONObject()
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

    public static ClientUtils.DataResponse updateProfile(String id, String token, String firstName, String lastName, String documentType, String documentExpiry, String email, String phone, String address, String dob, String gender, String country_of_birth) throws UnknownHostException, SocketTimeoutException {

        try {
            return ClientUtils.patchData(ServerConstants.API_UPDATE_PROFILE + id, token, new JSONObject()
                            .put("first_name", firstName)
                            .put("last_name", lastName)
                            .put("document_type", documentType)
                            .put("document_expiry", documentExpiry)
                            .put("email", email)
                            .put("phone", phone)
                            .put("address_1", address)
                            .put("address_2", "")
                            .put("address_3", "")
                            .put("dob", dob)
                            .put("gender", gender)
                            .put("country_of_birth", country_of_birth)
                    , TimeoutConstants.TIMEOUT_DEFAULT);
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (UnknownHostException e) {
            throw e;
        } catch (Exception e) {
            return new ClientUtils.DataResponse(ServerConstants.RESPONSE_ERROR);

        }

    }


    public enum EnumStatus {
        VERIFYING,
        PENDING,
        SUSPENDED,
        ACTIVE;

        public static EnumStatus parse(String string) {
            try {
                return valueOf(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return PENDING;
        }
    }
}
