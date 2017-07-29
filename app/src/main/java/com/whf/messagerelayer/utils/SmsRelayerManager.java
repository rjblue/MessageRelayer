package com.whf.messagerelayer.utils;

import java.util.List;

/**
 * Created by WHF on 2017/3/26.
 */

public class SmsRelayerManager {
    /**
     * 发送短信至目标手机号
     *
     * @param dataManager
     * @param content     短信内容
     */
    public static void relaySms(NativeDataManager dataManager, String content) {
        String objectMobile = dataManager.getObjectMobile();
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //文本超长短信要拆分发送
        List<String> dividedMsg = smsManager.divideMessage(content);
        if (dividedMsg != null) {
            for (String msg : dividedMsg) {
                smsManager.sendTextMessage(objectMobile, null, msg, null, null);
            }
        }
    }
}
