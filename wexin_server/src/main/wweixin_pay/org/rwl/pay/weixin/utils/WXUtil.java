package org.rwl.pay.weixin.utils;

import java.util.Random;

public class WXUtil {
    
    /**
     * 获取32位随机字符串
     * 
     * 作者: zhoubang 
     * 日期：2015年6月26日 下午3:51:44
     * @return
     */
    public static String getNonceStr() {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }

    /**
     * 时间戳
     * 
     * 作者: zhoubang 
     * 日期：2015年6月26日 下午3:52:08
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
    
}
