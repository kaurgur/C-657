package com.example.prabhjot.project_cis657;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by prabhjot on 6/20/2017.
 */

public class projectApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
