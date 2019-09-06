package com.qing.common.utils.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Date;

/**
 * @author guoqf
 * @date 2019/7/31 10:05
 */
@JacksonXmlRootElement(namespace = "xml")
public class XmlObj {

    @JacksonXmlProperty(localName = "an_int")
    private int anInt;

    @JacksonXmlProperty(localName = "as_str")
    private String aStr;

    @JacksonXmlProperty(localName = "a_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date aDate;

    @JacksonXmlProperty(localName = "a_bytes")
    private byte[] aBytes;

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public Date getaDate() {
        return aDate;
    }

    public void setaDate(Date aDate) {
        this.aDate = aDate;
    }

    public String getaStr() {
        return aStr;
    }

    public void setaStr(String aStr) {
        this.aStr = aStr;
    }

    public byte[] getaBytes() {
        return aBytes;
    }

    public void setaBytes(byte[] aBytes) {
        this.aBytes = aBytes;
    }
}
