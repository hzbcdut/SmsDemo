package com.example.huzhengbiao.newsmsdemo.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.huzhengbiao.newsmsdemo.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要功能:
 * author huzhengbiao
 * date : On 2018/10/14
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = SMSBroadcastReceiver.class.getSimpleName();

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private OnReceiveSMSListener mOnReceiveSMSListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.logInfo("debug", TAG + "  --> onReceive ()");
        if (intent.getAction().equals(SMS_RECEIVED_ACTION) && intent != null && intent.getExtras() != null) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for(Object pdu:pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte [])pdu);
                // 短信号码
                String sender = smsMessage.getDisplayOriginatingAddress();
                //短信内容
                String content = smsMessage.getDisplayMessageBody();
                LogUtil.logInfo("debug", TAG + " --> sender = " + sender + "  content = " + content);
                // 筛选
                if (content.contains("Spot") && mOnReceiveSMSListener!= null) {
                    mOnReceiveSMSListener.onReceived(content);
                    abortBroadcast();
                }
            }
        }
    }

    public void setOnReceiveSMSListener(OnReceiveSMSListener onReceiveSMSListener) {
        mOnReceiveSMSListener = onReceiveSMSListener;
    }


    /**
     * 回调接口
     */
    public interface OnReceiveSMSListener {
        void onReceived(String message);
    }

}
