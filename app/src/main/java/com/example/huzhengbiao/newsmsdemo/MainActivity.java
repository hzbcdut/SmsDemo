package com.example.huzhengbiao.newsmsdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.huzhengbiao.newsmsdemo.sms.SmsPresent;

public class MainActivity extends AppCompatActivity {

    private SmsPresent mSmsPresent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        mSmsPresent = new SmsPresent(new Handler(), this);

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
