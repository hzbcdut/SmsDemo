package com.example.huzhengbiao.newsmsdemo.sms;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
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
    private Handler mHandler;

    public SmsContentObserver(Handler handler, Context context) {
        super(handler);
        this.mContext = context.getApplicationContext();
        this.mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        LogUtil.logInfo("debug", TAG + "--> onChange(boolean selfChange, Uri uri)  selfChange = " + selfChange + " uri = " + uri);

        //查询短信之前先检查一下是否有短信读取权限, 用户没授权此权限时查询可以吗?
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED)  return;

//        showAllColumnName(mContext);

       String smsBody =  querySmsDb(mContext);
       String code = SmsUtil.getVerificationCode(smsBody);
       SmsUtil.returnSmsCode(mHandler, code);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        LogUtil.logInfo("debug", TAG + "--> onChange(boolean selfChange)  selfChange = " + selfChange );

    }


    public String querySmsDb(Context context) {
       return querySmsDb(context, Uri.parse("content://sms/inbox"));
    }

    /**
     * 查询手机短信
     */
    public String  querySmsDb(Context context, Uri uri) {
        // 读取收件箱中含有某关键词的短信
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, new String[] {
                        "_id", "address", "body", "read" }, "body like ? and read=?",
                new String[] { "%Spot%", "0" }, "date desc");

        String smsBody = "";
        if (cursor != null && cursor.moveToFirst()) {
            smsBody = cursor.getString(cursor.getColumnIndex("body"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            LogUtil.logDebug("debug", " --> address = " + address + smsBody);
            cursor.close();
        }

        return smsBody;
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
            String verificationCode = SmsUtil.getVerificationCode(smsbody);
        }

        // 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
        if (Build.VERSION.SDK_INT < 14) {
            cursor.close();
        }
    }


    /**
     * 查找短信数据库表的所有字段
     */

    private void showAllColumnName(Context context) {
        Uri uri = Uri.parse("content://sms/inbox");
        final Cursor cur = context.getContentResolver().query(uri, null, null, null, null);
        for (String s : cur.getColumnNames()){
            LogUtil.logInfo("COLUMN_NAME", s);
        }
    }
}
