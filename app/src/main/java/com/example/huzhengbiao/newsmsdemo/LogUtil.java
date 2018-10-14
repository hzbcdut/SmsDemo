package com.example.huzhengbiao.newsmsdemo;

import android.text.TextUtils;
import android.util.Log;

/**
 *
 */

public class LogUtil {
    public static void logError(String tag, String msg){
        if(Constants.LOG_DEBUG){
            if(TextUtils.isEmpty(msg)){
                return;
            }

            Log.e(tag, msg);
        }
    }

    public static void logInfo(String tag, String msg){
        if(Constants.LOG_DEBUG){
            if(TextUtils.isEmpty(msg)){
                return;
            }

            Log.i(tag, msg);
        }
    }

    public static void logDebug(String tag, String msg){
        if(Constants.LOG_DEBUG){
            if(TextUtils.isEmpty(msg)){
                return;
            }

            Log.d(tag, msg);
        }
    }

    public static void logVerbose(String tag, String msg){
        if(Constants.LOG_DEBUG){
            if(TextUtils.isEmpty(msg)){
                return;
            }

            Log.v(tag, msg);
        }
    }
}
