package com.example.huzhengbiao.newsmsdemo.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.huzhengbiao.newsmsdemo.LogUtil;

/**
 * 主要功能:
 * author huzhengbiao
 * date : On 2018/10/14
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = SMSBroadcastReceiver.class.getSimpleName();

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";


    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.logInfo("debug", TAG + "  --> onReceive ()");
    }
}
