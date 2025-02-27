package com.bronya.travel.Utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
     public static String encrypt(String data) {
         return DigestUtils.md5Hex(data);
     }
}
