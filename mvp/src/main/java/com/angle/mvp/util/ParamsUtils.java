package com.angle.mvp.util;


import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

import static com.angle.mvp.common.Constrant.RequestKey.*;
import static com.angle.mvp.common.Constrant.*;


/*
 * created by Cherry on 2019-12-27
 **/
public class ParamsUtils {

    private static String SHA1_KEY = "K;9)Bq|ScMF1h=Vp5uA-G87d(_fi[aP,.w^{vQ:W";


    public static HashMap<String,Object> getCommonParams(){

        HashMap<String,Object> hashMap = new HashMap();

        hashMap.put(KEY_FROM, VALUE_FROM);
        hashMap.put(KEY_LANG, VALUE_LANG);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        hashMap.put(KEY_NONCE, nonce);
        hashMap.put(KEY_TIMESTAMP, timestamp);
        hashMap.put(KEY_SIGNATURE,getSHA1(timestamp, nonce));

        return hashMap;
    }



    public static  String getSHA1(String timestamp, String nonce) {
        try {
            String[] array = new String[]{SHA1_KEY, timestamp, nonce};
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 3; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
