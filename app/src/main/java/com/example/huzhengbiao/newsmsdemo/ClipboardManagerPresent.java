package com.example.huzhengbiao.newsmsdemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 主要功能:
 * author huzhengbiao
 * date : On 2018/10/15
 */
public class ClipboardManagerPresent {
    private static final String TAG = ClipboardManagerPresent.class.getSimpleName();

    private Context mContext;
    private ClipboardManager mClipboardManager;
    private ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener;
    private OnClipboardSmsCodeListener mListener;

    public ClipboardManagerPresent(Context context, OnClipboardSmsCodeListener listener){
        this.mListener = listener;
        this.mContext = context.getApplicationContext();

        mClipboardManager = mClipboardManager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mOnPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {

                if (mClipboardManager.hasPrimaryClip() && mClipboardManager.getPrimaryClip().getItemCount() > 0) {
                    // 这里用正则匹配一下 剪贴板的内容是否只有四个数字
                    //然后通过回调返回， 返回之后用android-spot LoginFirstActivity的发送信息去发送内容

                    // 剪贴板的itemCount总共可以有多少个 ， 应该获取第几个
                    int itemCount = mClipboardManager.getPrimaryClip().getItemCount();
                    for (int i = 0; i< itemCount; i++) {

                        ClipData.Item item  = mClipboardManager.getPrimaryClip().getItemAt(i);

                        String text = item.coerceToHtmlText(mContext).toString();

                        LogUtil.logInfo("debug", TAG + " ---> 剪贴板内容 =  " + text);

                        String regex = "[0-9]{4}";
                        // Pattern.matches() 匹配全部text内容， 只有四个数字
                        if (Pattern.matches(regex, text)) {
                            mListener.onSmsCode(text);
                        }
                    }

                }

            }
        };
        mClipboardManager.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener);
    }



    public void destroy() {
        mClipboardManager.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener);
    }

    public interface OnClipboardSmsCodeListener{
        void onSmsCode(String code);
    }
}
