package com.example.huzhengbiao.newsmsdemo.sms;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.huzhengbiao.newsmsdemo.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要功能:
 * author huzhengbiao
 * date : On 2018/10/14
 */
public class SmsContentObserver extends ContentObserver{

    public static final String TAG = SmsContentObserver.class.getSimpleName();

    private Context mContext;

    public SmsContentObserver(Handler handler, Context context) {
        super(handler);
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        LogUtil.logInfo("debug", TAG + "--> onChange(boolean selfChange, Uri uri)  selfChange = " + selfChange + " uri = " + uri);


    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        LogUtil.logInfo("debug", TAG + "--> onChange(boolean selfChange)  selfChange = " + selfChange );

    }



    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public void querySmsDb(Context context) {
        querySmsDb(context, Uri.parse("content://sms/inbox"));
    }

    /**
     * 查询手机短信
     */
    public void  querySmsDb(Context context, Uri uri) {
        Log.d("debug", "onChange(boolean selfChange)");

        Cursor c = context.getContentResolver().query(uri, null, null, null, "date desc");
        if (c != null) {
            while (c.moveToNext()) {
                Date d = new Date(c.getLong(c.getColumnIndex("date")));
                String date = dateFormat.format(d);
                StringBuilder sb = new StringBuilder();
                sb.append("发件人手机号码: " + c.getString(c.getColumnIndex("address")))
                        .append("信息内容: " + c.getString(c.getColumnIndex("body")))
                        .append(" 是否查看: " + c.getInt(c.getColumnIndex("read")))
                        .append(" 类型： " + c.getInt(c.getColumnIndex("type"))).append(date);

                LogUtil.logInfo("debug", " --> length = " + sb.toString().length() + "  sb.toString() = " + sb.toString());
                LogUtil.logInfo("debug", "--》 c.getInt(c.getColumnIndex(\"read\")) = " + c.getInt(c.getColumnIndex("read")));
            }

            c.close();
        }
    }



    /**
     * 使用Activity的managerQuery来查询
     * @param activity
     */
    private void querySmsContent(Activity activity) {
        Cursor cursor= activity.managedQuery(Uri.parse("content://sms/inbox"),
                new String[]{"_id","address","body","read"},
                " address=? and read=?",
                new String[] { "address", "0" }, "_id desc");

        // 按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
        if (cursor!=null&&cursor.getCount()>0){
            ContentValues values=new ContentValues();
            if (Build.VERSION.SDK_INT < 21){
                values.put("type",1);//修改短信为已读短信   5.0后以不能修改
            }
            cursor.moveToNext();
            int smsbodyColumn=cursor.getColumnIndex("body");
            String smsbody=cursor.getString(smsbodyColumn);

            //调用下面的截取短信中六位数字验证码的方法
            String verificationCode = getDynamicPassword(smsbody);
        }

        // 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
        if (Build.VERSION.SDK_INT < 14) {
            cursor.close();
        }
    }



    /**
     * 从字符串中截取连续6位数字组合 ([0-9]{" + 6 + "})截取六位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
     *
     * @param st 短信内容
     * @return 截取得到的6位动态密码
     */
    public String getDynamicPassword(String st){
        //  6是验证码的位数一般为六位   如果验证码的位数变化只要将6修改为想要的位数，
        // 过验证如果不止为数字，直接修改正则为想要的内容即可

        //Pattern是java.util.regex（一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包）中的一个类。
        // 一个Pattern是一个正则表达式经编译后的表现模式
        Pattern pattern= Pattern.compile("(?<![0-9])([0-9]{" + 6 + "})(?![0-9])") ;
        Matcher matcher=pattern.matcher(st);
        String dynamicPassword=null;
        while (matcher.find()){
            System.out.println(matcher.group());
            dynamicPassword=matcher.group();
        }
        return dynamicPassword;
    }
}
