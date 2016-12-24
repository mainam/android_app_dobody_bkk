package com.dobody.bkk;

import android.app.Application;
import android.content.Context;

/**
 * Created by Admin on 12/20/2016.
 */

public class MainApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MainApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MainApplication.context;
    }
}
