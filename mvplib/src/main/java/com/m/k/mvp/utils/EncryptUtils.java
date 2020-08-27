package com.m.k.mvp.utils;

import android.util.Base64;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @desc: AES对称加密，对明文进行加密、解密处理
 * @author: yong.li
 * @createTime: 2018年8月28日 上午9:54:52
 * @version: v0.0.1
 */
public class EncryptUtils {
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * @desc: AES对称-加密操作
     * @author: yong.li
     * @createTime: 2018年8月28日 上午10:25:50
     * @version: v0.0.1
     * @param keyStr 进行了Base64编码的秘钥
     * @param data 需要进行加密的原文
     * @return String 数据密文，加密后的数据，进行了Base64的编码
     */
    public static String encrypt(String keyStr, String data) throws Exception {
        // 转换密钥
        Key key = new SecretKeySpec(Base64.decode(keyStr,Base64.DEFAULT), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(data.getBytes());

        return Base64.encodeToString(result,Base64.DEFAULT);
    }

    /**
     * @desc: AES对称-解密操作
     * @author: yong.li
     * @createTime: 2018年8月28日 上午10:22:47
     * @version: v0.0.1
     * @param keyStr 进行了Base64编码的秘钥
     * @param data 需要解密的数据<span style="color:red;">（数据必须是通过AES进行加密后，对加密数据Base64编码的数据）</span>
     * @return String 返回解密后的原文
     */
    public static String decrypt(String keyStr, String data) throws Exception {
        // 转换密钥
        Key key = new SecretKeySpec(Base64.decode(keyStr,Base64.DEFAULT), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 解密
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(Base64.decode(data,Base64.DEFAULT));
        return new String(result);
    }



    /**
     * @desc: 生成AES的秘钥，秘钥进行了Base64编码的字符串
     * @author: yong.li
     * @createTime: 2018年8月28日 上午10:24:32
     * @version: v0.0.1
     * @return String 对生成的秘钥进行了Base64编码的字符串
     */
    public static String keyGenerate(String key) throws Exception {
        // 生成密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(128,new SecureRandom(key.getBytes()));
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        return Base64.encodeToString(keyBytes,Base64.DEFAULT);
    }
}

