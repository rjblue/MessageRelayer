package com.whf.messagerelayer.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.whf.messagerelayer.confing.Constant;
import com.whf.messagerelayer.service.SmsService;
import com.whf.messagerelayer.utils.FormatMobile;
import com.whf.messagerelayer.utils.NativeDataManager;

public class MessageReceiver extends BroadcastReceiver {

    private NativeDataManager mNativeDataManager;
    public MessageReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mNativeDataManager = new NativeDataManager(context);
        if(mNativeDataManager.getReceiver()){
            Bundle bundle = intent.getExtras();
            if(bundle!=null){
                Object[] pdus = (Object[]) bundle.get("pdus");
                String mobile = null;
                StringBuffer contents = new StringBuffer();
                //超长短信会被拆分,先合并起来方便后续业务逻辑处理
                for(int i = 0;i<pdus.length;i++){
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    mobile = sms.getDisplayOriginatingAddress();
                    contents.append(sms.getMessageBody());
                }
                startSmsService(context, mobile, contents.toString());
            }
        }
    }

    private ComponentName startSmsService(Context context, String mobile, String content) {
        if(FormatMobile.hasPrefix(mobile)){
            mobile = FormatMobile.formatMobile(mobile);
        }
        Intent serviceIntent = new Intent(context, SmsService.class);
        serviceIntent.putExtra(Constant.EXTRA_MESSAGE_CONTENT,content);
        serviceIntent.putExtra(Constant.EXTRA_MESSAGE_MOBILE,mobile);
        return context.startService(serviceIntent);
    }


}
