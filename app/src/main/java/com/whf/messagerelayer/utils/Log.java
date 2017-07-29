package com.whf.messagerelayer.utils;

import com.whf.messagerelayer.activity.MainActivity;

/**
 * Created by lingxuan on 2017/7/29.
 */
public class Log {
    public static void log(String msg) {
        android.util.Log.d("sms", msg);
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null) {
            mainActivity.log(msg);
        }
    }
}
