
package com.bgd.support.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 *  
 * @ClassName:  PasswordUtil   
 * @Description:TODO 密码工具
 * @author: JackMore
 * @date:   2019年2月25日 下午2:11:57
 */
public class PasswordUtil {
    private static final String DEFAULT_SALT = "match201820182018match";

    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    private static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new BigInteger(1, bs).toString(16);
            //return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    public static String MD5Salt(String password) {
        return md5Hex(md5Hex(password) + DEFAULT_SALT);
    }

    public static String MD5Salt(String password, String salt) {
        String psalt = md5Hex(password) + salt;
        return md5Hex(psalt);
    }


	/*
	 * public static void main(String[] ar){ String admin = MD5Salt("admin");
	 * System.out.println("admin---"+admin); }
	 */
}
