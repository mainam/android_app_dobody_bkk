package com.dobody.bkk.constant;

import okhttp3.MediaType;

/**
 * Created by MaiNam on 11/30/2016.
 */

public class ServerConstants {
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_ERROR = 404;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String getServerLink() {
        return null;
    }
}
