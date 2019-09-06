package com.qing.common.utils.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author guoqf
 * @date 2019/7/30 22:53
 */

public class XmlUtil {
    public static String beanToXml(Object bean) throws JsonProcessingException {
        final XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(bean);
    }

    public static <T> T xmlToBean(String xml, Class<T> tClass) throws IOException {
        final XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xml, tClass);
    }

    public static void main(String[] args) {
//        String url = "select * from \"public\".\"test\" limit 1000 offset 0";
////        String url = "http://10.1.198.66:7082/uac/web3/jsp/goldbank/goldbank3!goToSysIframe.action?platformVersion=0&gpid=08a4664efb6b413888a0ab701bf4e99d&operId=0d5da1072f8848d2a1d561848cd7c97b&resAcctId=1000792&operCode=10280&busyType=3&systemId=100105&mainLoginName=8|-85|41|47|32|-118|92|16|-18&sensitiveColumn=false&appCode=UAP&tableNames=\"test\"&dbName=postgres&pracct=dogee&slacct=postgres&resNumber=100105&userIp=10.19.19.16&tokenId=08a4664efb6b413888a0ab701bf4e99d";
//        final String trimmedUrl = trimSign(url);
//        System.out.println(trimmedUrl);
        BigDecimal bigDecimal = new BigDecimal(111);
        final Integer integer = new Integer(1111);


        System.out.println(integer instanceof  Number);
        System.out.println(((Number)integer).longValue());

    }
    public static byte[] int2byte(int number){
        byte[] byt = new byte[4];
        byt[0] = (byte)(number&0xff);
        byt[1] = (byte)(number>>8&0xff);
        byt[2] = (byte)(number>>16&0xff);
        byt[3] = (byte)(number>>24&0xff);
        return byt;
    }


    private static final String END_COMMONT = "******/";
    private static final String START_COMMONT = "/******";
    /**
     * 空字符串
     */
    private static String EMPTY = "";
    /**
     * 双引号
     */
    private static final String DOUBLE_QUOTATION_MARKS = "\"";
    /**
     * 单引号
     */
    private static final String SINGLE_QUOTATION_MARKS = "\'";
    /**
     * 反单引号
     */
    private static final String BACK_QUOTE = "`";
    /**
     * [] 可以同时去除
     */
    private static final String BRACKET = "[\\[\\]]";
    private static final String LINE_SEPERATOR = "\r\n";

    public static String trimSign(String originStr) {
        System.out.println("原SQL是：" + originStr);
        if (StringUtils.isEmpty(originStr)) {
            return null;
        }
        if (originStr.contains(START_COMMONT) && originStr.contains(END_COMMONT)) {
            int beginIndex = originStr.indexOf(END_COMMONT);
            originStr = originStr.substring(beginIndex + 7, originStr.length() - 1);
        }

        originStr = originStr.contains(DOUBLE_QUOTATION_MARKS) == Boolean.TRUE ? originStr.replaceAll(DOUBLE_QUOTATION_MARKS, EMPTY) : originStr;
        originStr = originStr.contains(SINGLE_QUOTATION_MARKS) == Boolean.TRUE ? originStr.replaceAll(SINGLE_QUOTATION_MARKS, EMPTY) : originStr;
        originStr = originStr.contains(BACK_QUOTE) == Boolean.TRUE ? originStr.replaceAll(BACK_QUOTE, EMPTY) : originStr;
        originStr = originStr.contains(LINE_SEPERATOR) == Boolean.TRUE ? originStr.replaceAll(LINE_SEPERATOR, EMPTY) : originStr;
        originStr = originStr.replaceAll(BRACKET, EMPTY);
        System.out.println("去除特殊字符后SQL: " + originStr);
        return originStr.trim();
    }
}
