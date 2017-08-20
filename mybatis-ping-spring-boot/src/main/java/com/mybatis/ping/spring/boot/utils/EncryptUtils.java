package com.mybatis.ping.spring.boot.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具
 * Created by liujiangping on 2017/5/2.
 */
public class EncryptUtils {

    /**
     * md5加密
     * @param code 需要加密的代码
     * @return
     */
    public static String md5(String code) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(code.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}