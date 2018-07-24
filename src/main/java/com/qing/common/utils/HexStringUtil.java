package com.qing.common.utils;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Qing on 2017/4/26.
 */
public class HexStringUtil {
    public static String byteArrayToHexString(byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            throw new IllegalArgumentException("The byte array must not be empty.");
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s);


        }
        return sb.toString();

    }

    private static final String hexStringBase = "0123456789abcdef";

    public static byte[] hexStringToByteArray(String hexString) {
        if (StringUtils.isBlank(hexString)) {

            throw new IllegalArgumentException("The hex string must have text.");
        }

        int len = hexString.length() / 2;
        char[] chars = hexString.toLowerCase().toCharArray();
        byte[] bytes = new byte[len];
        int pos;
        for (int i = 0; i < len; i++) {
            pos = i * 2;
            byte b = (byte) (hexStringBase.indexOf(chars[pos]) << 4 | hexStringBase.indexOf(chars[pos + 1]));
            bytes[i] = b;
        }

        return bytes;
    }

}
