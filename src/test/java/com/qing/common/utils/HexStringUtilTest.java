package com.qing.common.utils;


import org.junit.Test;

/**
 * Created by Guoqingfeng on 2017/9/30.
 */
public class HexStringUtilTest extends SuperTest {


    @Test
    public void testBytesToHex() {
        byte[] bytes = new byte[100];
        int i = 0;
        for (; i < 100; i++) {
            bytes[i] = (byte) i;
        }
        String hex = HexStringUtil.byteArrayToHexString(bytes);
        System.out.println("Hex: " + hex);
    }

    @Test
    public void testHexToBytes() {
        String hex = "01233333333333333333333452345asdfasgasgf4a5df1a6s15f8asdf25sadf56adsfaaaaa";
        byte[] bytes = HexStringUtil.hexStringToByteArray(hex);
        for (byte b : bytes) {
            System.out.println("Byte: " + b);
        }
    }
}
