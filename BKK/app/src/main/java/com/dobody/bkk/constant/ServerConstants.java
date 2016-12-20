package com.dobody.bkk.constant;

import okhttp3.MediaType;

/**
 * Created by MaiNam on 11/30/2016.
 */

public class ServerConstants {
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_ERROR = 401;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String API_LOGIN = "users/login";
    public static final String API_REGISTER= "users";
    public static final int STATUS_201 = 201;
    public static final String API_VERIFY = "users/verify";
    public static final String API_RESEND = "users/resend";
    public static final String API_UPDATE_PROFILE ="users/:" ;

    //    public static String getServerLink() {
//        return "http://zeitmedia.vn/forexapi/public/";
//    }
    public static String getServerLink() {
        return "https://103.11.140.83:8888/";
    }
}
