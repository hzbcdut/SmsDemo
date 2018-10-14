package com.example.huzhengbiao.newsmsdemo.sms;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;

/**
 * 主要功能:
 * author huzhengbiao
 * date : On 2018/10/14
 */
public class SmsPresent {

    private Uri mUri = Uri.parse("content://sms/inbox");

    private Context mContext;

    private ContentResolver mContentResolver;
    private SmsContentObserver mSmsContentObserver;

    private SMSBroadcastReceiver mSMSBroadcastReceiver;


    public SmsPresent(@NonNull Handler handler, @NonNull Context context) {
        this.mContext = context;
        mSmsContentObserver = new SmsContentObserver(handler);
        mContentResolver =  context.getContentResolver();
        //注册短信变化监听
        mContentResolver.registerContentObserver(mUri,true, mSmsContentObserver);

        // 注册广播
        IntentFilter intentFilter = new IntentFilter(SMSBroadcastReceiver.SMS_RECEIVED_ACTION);
       // 设置优先级
        intentFilter.setPriority(Integer.MAX_VALUE);

        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        context.registerReceiver(mSMSBroadcastReceiver,intentFilter);
    }


    public void destroy(){
        //关闭数据库监听
        mContentResolver.unregisterContentObserver(mSmsContentObserver);

        mContext.unregisterReceiver(mSMSBroadcastReceiver);
    }

}
