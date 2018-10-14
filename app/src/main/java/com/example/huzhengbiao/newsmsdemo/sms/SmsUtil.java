package com.example.huzhengbiao.newsmsdemo.sms;

import android.os.Handler;
import android.os.Message;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要功能:
 * author huzhengbiao
 * date : On 2018/10/14
 */
public class SmsUtil {

    public static final int MESSAGE_CODE = 1021;
    /**
     * 根据短信内容提取出验证码
     * 从字符串中截取连续4位数字组合 ([0-9]{" + 4 + "})截取四位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
     *
     * @param smsContent 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getVerificationCode(String smsContent) {
        Pattern pattern= Pattern.compile("(?<![0-9])([0-9]{" + 4 + "})(?![0-9])") ;
        Matcher matcher=pattern.matcher(smsContent);
        String dynamicPassword="";
        while (matcher.find()){
            System.out.println(matcher.group());
            dynamicPassword=matcher.group();
        }
        return dynamicPassword;
    }

    public static void returnSmsCode(Handler handler, String code) {
        if (handler != null) {
            Message message = handler.obtainMessage();
            message.what = MESSAGE_CODE;
            message.obj = code;
            message.sendToTarget();

            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
