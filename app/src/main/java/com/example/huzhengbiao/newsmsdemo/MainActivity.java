package com.example.huzhengbiao.newsmsdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.huzhengbiao.newsmsdemo.sms.SmsPresent;
import com.example.huzhengbiao.newsmsdemo.sms.SmsUtil;

public class MainActivity extends AppCompatActivity {

    private SmsPresent mSmsPresent;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        handler = new SmsHandler();
        mSmsPresent = new SmsPresent(handler, this);
    }


    public static class SmsHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SmsUtil.MESSAGE_CODE:
                    LogUtil.logDebug("debug", " --> MainActivity code = " + msg.obj);
                    String code = (String) msg.obj;

                    // TODO: 2018/10/14  这里来处理短信验证码， 但是因为采用的方案是通过两种方式获取的短信
                    // 业务逻辑是收到短信并更新之后，就会finish此页面的话
                    // 所以在这里要判断是否Activity或Fragment已经销毁了， 才能去做更新UI的操作。
                    break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSmsPresent.destroy();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS}, 1000);
        } else {
            // todo 已经有此权限了，可以做点什么
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions == null || permissions.length <= 0) return;
        if (requestCode == 1000) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                //申请权限 用户拒绝权限 小米 只要拒绝一次 就不会再弹窗
//                ToastUtil.showToast(getApplicationContext(),"no permission");
                finish();
            }
        }
    }
}
