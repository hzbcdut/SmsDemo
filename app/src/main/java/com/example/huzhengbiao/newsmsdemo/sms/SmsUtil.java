package com.example.huzhengbiao.newsmsdemo.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要功能:
 * author huzhengbiao
 * date : On 2018/10/14
 */
public class SmsUtil {

    /**
     * 根据短信内容提取出验证码
     * @param smsContent
     * @return
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
}
