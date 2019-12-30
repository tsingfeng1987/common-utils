package com.qing.common;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ByteUtil {
    public ByteUtil() {
    }

    public static byte[] short2Byte(short a) {
        byte[] b = new byte[]{(byte)(a >> 8), (byte)a};
        return b;
    }

    public static void short2Byte(short a, byte[] b, int offset) {
        b[offset] = (byte)(a >> 8);
        b[offset + 1] = (byte)a;
    }

    public static short byte2Short(byte[] b) {
        return (short)((b[0] & 255) << 8 | b[1] & 255);
    }

    public static int byteToInt(byte[] b) {
        return b[0] & 255 | (b[1] & 255) << 8 | (b[2] & 255) << 16;
    }

    public static int byte2int_le(byte[] bt, int pos) {
        int size = bt[pos] & 255;
        size |= (bt[pos + 1] & 255) << 8;
        size |= (bt[pos + 2] & 255) << 16;
        size |= (bt[pos + 3] & 255) << 24;
        return size;
    }

    public static short byte2Short(byte[] b, int offset) {
        return (short)((b[offset] & 255) << 8 | b[offset + 1] & 255);
    }

    public static void long2Byte(long a, byte[] b, int offset) {
        b[offset + 0] = (byte)((int)(a >> 56));
        b[offset + 1] = (byte)((int)(a >> 48));
        b[offset + 2] = (byte)((int)(a >> 40));
        b[offset + 3] = (byte)((int)(a >> 32));
        b[offset + 4] = (byte)((int)(a >> 24));
        b[offset + 5] = (byte)((int)(a >> 16));
        b[offset + 6] = (byte)((int)(a >> 8));
        b[offset + 7] = (byte)((int)a);
    }

    public static long byte2Long(byte[] b, int offset) {
        return ((long)b[offset + 0] & 255L) << 56 | ((long)b[offset + 1] & 255L) << 48 | ((long)b[offset + 2] & 255L) << 40 | ((long)b[offset + 3] & 255L) << 32 | ((long)b[offset + 4] & 255L) << 24 | ((long)b[offset + 5] & 255L) << 16 | ((long)b[offset + 6] & 255L) << 8 | ((long)b[offset + 7] & 255L) << 0;
    }

    public static long byte2Long(byte[] b) {
        return (long)((b[0] & 255) << 56 | (b[1] & 255) << 48 | (b[2] & 255) << 40 | (b[3] & 255) << 32 | (b[4] & 255) << 24 | (b[5] & 255) << 16 | (b[6] & 255) << 8 | b[7] & 255);
    }

    public static byte[] long2Byte(long a) {
        byte[] b = new byte[]{(byte)((int)(a >> 56)), (byte)((int)(a >> 48)), (byte)((int)(a >> 40)), (byte)((int)(a >> 32)), (byte)((int)(a >> 24)), (byte)((int)(a >> 16)), (byte)((int)(a >> 8)), (byte)((int)(a >> 0))};
        return b;
    }

    public static int byte2Int(byte[] b) {
        return (b[0] & 255) << 24 | (b[1] & 255) << 16 | (b[2] & 255) << 8 | b[3] & 255;
    }

    public static int byte2Int(byte[] b, int offset) {
        return (b[offset++] & 255) << 24 | (b[offset++] & 255) << 16 | (b[offset++] & 255) << 8 | b[offset++] & 255;
    }

    public static byte[] int2Byte(int a) {
        byte[] b = new byte[]{(byte)(a >> 24), (byte)(a >> 16), (byte)(a >> 8), (byte)a};
        return b;
    }

    public static void int2Byte(int a, byte[] b, int offset) {
        b[offset++] = (byte)(a >> 24);
        b[offset++] = (byte)(a >> 16);
        b[offset++] = (byte)(a >> 8);
        b[offset++] = (byte)a;
    }

    public static int onebyte2int(byte[] b, int off) {
        return 255 & b[off];
    }

    public static void int2onebyte(int n, byte[] b, int off) throws Exception {
        if (n >= 0 && n <= 255) {
            b[off] = 0;
            b[off] = (byte)(b[off] | n);
        } else {
            throw new Exception(n + " exceed unsigned char range [0, 255]!");
        }
    }

    public static void int2twobyte(int n, byte[] b, int off, boolean isLowerBefore) throws Exception {
        if (n >= 0 && n <= 65535) {
            if (isLowerBefore) {
                b[off] = (byte)(n >> 8);
                b[off + 1] = (byte)n;
            } else {
                b[off + 1] = (byte)(n >> 8);
                b[off] = (byte)n;
            }

        } else {
            throw new Exception(n + " exceed unsigned char range [0, 65535]!");
        }
    }

    public static byte[] hexStr2Bytes(String s) {
        String[] strings = s.split(" ");
        byte[] data = new byte[strings.length];

        for(int i = 0; i < strings.length; ++i) {
            data[i] = (byte)((Character.digit(strings[i].charAt(0), 16) << 4) + Character.digit(strings[i].charAt(1), 16));
        }

        return data;
    }

    public static byte[] hexStr2BytesWithoutSpace(String s) {
        char[] chars = s.toCharArray();

        byte[] data = new byte[chars.length >>> 1];

        for(int i = 0; i < data.length; ++i) {
            data[i] = (byte) ((Character.digit(chars[2 * i], 16) << 4) + Character.digit(chars[2 * i + 1], 16));
        }

        return data;
    }

    public static final String toHexString(byte[] b) {
        String hexChar = "0123456789ABCDEF";
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            sb.append("0123456789ABCDEF".charAt(b[i] >> 4 & 15));
            sb.append("0123456789ABCDEF".charAt(b[i] & 15));
            sb.append(" ");
        }

        return sb.toString();
    }

    public static String toHexString(String str) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < str.length(); ++i) {
            int ch = str.charAt(i);
            sb.append(Integer.toHexString(ch)).append(" ");
        }

        return sb.toString();
    }

    public static boolean isEmpty(byte[] bytes) {
        return bytes == null || bytes.length < 1;
    }

    public static String byte2String(byte[] bytes, String encode) {
        if (!isEmpty(bytes) && !StringUtils.isEmpty(encode)) {
            String returnString = null;

            try {
                returnString = new String(bytes, encode);
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            }

            return returnString;
        } else {
            return null;
        }
    }

    public static byte[] listByte2bytes(List<Byte> listByte) {
        byte[] bytes = new byte[listByte.size()];

        for(int i = 0; i < listByte.size(); ++i) {
            bytes[i] = (Byte)listByte.get(i);
        }

        return bytes;
    }
}
