package com.qing.common.utils.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qing.common.utils.SuperTest;
import com.qing.common.utils.model.XmlObj;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author guoqf
 * @date 2019/7/31 10:05
 */
public class XmlUtilTest extends SuperTest {

    @Test
    public void testObj2Xml() {
        final XmlObj xmlObj = new XmlObj();
        final byte[] aBytes = {1, 2, 3};
        xmlObj.setaBytes(aBytes);
        xmlObj.setaDate(new Date());
        xmlObj.setAnInt(11111);
        xmlObj.setaStr("112324567890");
        try {
            final String s = XmlUtil.beanToXml(xmlObj);
            System.out.println("xml: " + s);

            final XmlObj xmlObj1 = XmlUtil.xmlToBean(s, XmlObj.class);
            System.out.println("XmlObj1: " + xmlObj1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /*public static void main(String[] args) {

        final String zPattern = "yyyy-MM-dd HH:mm:ss z";
//        String str = "2019-07-30 14:30:55 EDT";
//        String str = "2019-07-30 14:30:55 CST";
        final Date current = new Date();
        final String s = current.toGMTString();
        System.out.println("toGMTString: "+s);
        String str = new SimpleDateFormat(zPattern).format(current);
        System.out.println(str);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(zPattern);
        try {
            final Date parsed = dateFormat.parse(str);
            System.out.println(parsed);
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            final String formatted = format.format(parsed);
            System.out.println(formatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) {
//        boolean matches = "".matches(".*");
//        System.out.println(matches);
//        System.out.println(getYearMonth());
        String a = "我们a";
        byte[] bytes = a.getBytes();
        System.out.println(a.length());
    }

    public static String getYearMonth() {
        Calendar calendar = Calendar.getInstance();
        DateFormat format1 = new SimpleDateFormat("yyyyMM");
        Date date = calendar.getTime();
        String str = new String();
        str = format1.format(date);
        return str;
    }
}
