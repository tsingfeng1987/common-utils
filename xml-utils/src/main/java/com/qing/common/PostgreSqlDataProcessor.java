package com.qing.common;



import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author guoqf
 * @date 2019/12/24 11:43
 */
public class PostgreSqlDataProcessor {

    private static final String SINGLE_LINE_COMMENT_SYMBOL = "--";
    private static final String MULTIPLE_LINE_COMMENT_START_SYMBOL = "/*";
    private static final String MULTIPLE_LINE_COMMENT_END_SYMBOL = "*/";

    private static final byte[] SINGLE_LINE_COMMENT_BYTES = SINGLE_LINE_COMMENT_SYMBOL.getBytes();
    private static final byte[] MULTIPLE_LINE_COMMENT_START_BYTES = MULTIPLE_LINE_COMMENT_START_SYMBOL.getBytes();
    private static final byte[] MULTIPLE_LINE_COMMENT_END_BYTES = MULTIPLE_LINE_COMMENT_END_SYMBOL.getBytes();
    private static final byte BYTE_IN_HEX_0 = 0x00;
    private static final byte BYTE_IN_HEX_1 = 0x01;


    public static byte[] parseRealSqlData(byte[] data) {
        //根据单行注释查找长度下标
        int lenIndex = indexOfSqlLengthBySingleLineComment(data);
        if (lenIndex < 0) {
            //根据多行注释查找长度下标
            lenIndex = indexOfSqlLengthByMultiLineComment(data);
            if (lenIndex < 0) {
                //根据关键词（SQL命令）查找长度下标
                lenIndex = indexOfSqlLengthByKeyWords(data);
            }
        }
        if (lenIndex >= 0 && lenIndex < data.length) {
            int len = data[lenIndex];
            if (len > 0 && lenIndex + len <= data.length) {
                //允许SQL语句后没有数据
                int from = lenIndex + 1;
                return Arrays.copyOfRange(data, from, from + len);
            }
        }

        return null;
    }


    public static int getByteIndexOf(byte[] sources, byte[] src, int startIndex, int endIndex) {
        if (sources == null || src == null || sources.length == 0 || src.length == 0) {
            return -1;
        }
        if (endIndex > sources.length) {
            endIndex = sources.length;
        }

        int i, j;
        for (i = startIndex; i < endIndex; i++) {
            if (sources[i] == src[0] && i + src.length < endIndex) {
                for (j = 1; j < src.length; j++) {
                    if (sources[i + j] != src[j]) {
                        break;
                    }
                }

                if (j == src.length) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int indexOfSqlLengthByKeyWords(byte[] sqlData) {
        int startPos;
        String sqlKey = "CREATE,INSERT,UPDATE,DELETE,SELECT,ALTER,DROP,TRUNCATE,COMMENT,MERGE,DECLARE,BEGIN,GRANT,REVOKE,CALL,EXPLAIN PLAN,LOCK TABLE,RENAME";
        String[] keys = sqlKey.split(",");
        int sqlOptimizePox = -1;
        for (String key : keys) {

            startPos = indexOfSqlLength(sqlData, key.toLowerCase().getBytes());
            if (startPos == -1) {
                startPos = indexOfSqlLength(sqlData, key.toUpperCase().getBytes());
            }
            if (startPos > 0) {
                if (sqlOptimizePox == -1) {
                    sqlOptimizePox = startPos;
                }
                if (startPos > (sqlData.length / 3)) {
                    if (startPos < sqlOptimizePox) {
                        sqlOptimizePox = startPos;
                    }
                } else {
                    if (startPos < sqlOptimizePox) {
                        sqlOptimizePox = startPos;
                        break;
                    }
                }
            }
        }
        return sqlOptimizePox;
    }


    public static int indexOfSqlLengthByMultiLineComment(byte[] sqlData) {
        return indexOfSqlLength(sqlData, MULTIPLE_LINE_COMMENT_START_BYTES);
    }

    public static int indexOfSqlLengthBySingleLineComment(byte[] sqlData) {
        return indexOfSqlLength(sqlData, SINGLE_LINE_COMMENT_BYTES);
    }


    private static int indexOfSqlLength(byte[] sqlData, byte[] bytesToFound) {
        int startPos = getByteIndexOf(sqlData, bytesToFound, 0, sqlData.length - 1);
        if (startPos > 1) {

            int lenIndex = startPos - 1;
            int len = sqlData[lenIndex];
            //检查长度是否有效，以及长度字节的上一个字节是否为0x00或0x01
            int prefixIndex = lenIndex - 1;
            boolean endFlagPrefix = sqlData[prefixIndex] == BYTE_IN_HEX_0 || sqlData[prefixIndex] == BYTE_IN_HEX_1;
            if (len <= 0 || !endFlagPrefix) {
                return -1;
            }

            int postfixIndex = startPos + len;
            if (postfixIndex == sqlData.length) {
                //sql语句后面没有其他数据，返回index
                return lenIndex;
            } else if (postfixIndex < sqlData.length) {
                //SQL语句后还有数据，则判断SQL语句后面的第一个字节是否为0x00或0x01
                boolean endFlagPostfix = sqlData[postfixIndex] == BYTE_IN_HEX_0 || sqlData[postfixIndex] == BYTE_IN_HEX_1;
                if (endFlagPostfix) {
                    return lenIndex;
                }
            }
        }
        return -1;
    }


    public static void main(String[] args) throws IOException {
/*
        String s = "510000002173656C656374202A2066726F6D206364725F63616C6C5F64733B0D0A00";
        System.out.println("'"+parseSQL(ByteUtil.hexStr2BytesWithoutSpace(s))+"'");
        String line = "50 00 00 00 2A 00 73 65 6C 65 63 74 20 2A 20 66 72 6F 6D 20 61 73 69 61 69 6E 66 6F 2E 63 64 72 5F 63 61 6C 6C 5F 64 73 00 00";
//        String line = "50 00 00 00 2A 00 73 65 6C 65 63 74 20 2A 20 66 72 6F 6D 20 61 73 69 61 69 6E 66 6F 2E 63 64 72 5F 63 61 6C 6C 5F 64 73 00 00 00 42 00 00 00 0C 00 00 00 00 00 00 00 00 44 00 00 00 06 50 00 45 00 00 00 09 00 00 00 00 64 53 00 00 00 04";
        byte[] bytes = ByteUtil.hexStr2Bytes(line);
        System.out.println("'"+parseSQL(bytes)+"'");*/

        System.out.println(CharUtils.compare('\t',CharUtils.LF));



        parseMultiLines();
/*
        String hex = "00 B1 00 00 06 00 00 00 00 00 11 69 2B 01 01 00 00 00 01 00 00 00 03 5E 2C 61 80 00 00 00 00 00 00 01 20 00 00 00 01 0D 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 01 01 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 20 2D 2D D7 A2 CA CD 0A 73 65 6C 65 63 74 20 2A 20 66 72 6F 6D 20 75 61 70 5F 6E 6F 74 69 63 65 0A 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
//        String singleComment = "00 B1 00 00 06 00 00 00 00 00 11 69 2B 01 01 00 00 00 01 00 00 00 03 5E 2C 61 80 00 00 00 00 00 00 01 20 00 00 00 01 0D 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 01 01 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 20 2D 2D D7 A2 CA CD 0A 73 65 6C 65 63 74 20 2A 20 66 72 6F 6D 20 75 61 70 5F 6E 6F 74 69 63 65 0A 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
        printHexToString(hex);*/

    }
    private final static int SQLFLAG = 0x51;
    private final static int SQL_FLAG_50 = 0x50;
    private final static int SQL_EXTRA_LEN = 5;
    private static String parseSQL(byte[] bytes) {
        String sql = "";
        try{
            if (bytes[0] == SQLFLAG || bytes[0] == SQL_FLAG_50) {

                String strRead = new String(bytes);
                char[] data = strRead.toCharArray();
                sql = String.copyValueOf(data, SQL_EXTRA_LEN, bytes.length - SQL_EXTRA_LEN);
            }

            if (sql.startsWith("(") && sql.endsWith(")")) {
                sql = sql.substring(1, sql.length() - 1);
            }
        }catch(Exception e){
            e.printStackTrace();

        }

        return sql;
    }

    private static void parseMultiLines() throws IOException {
        String srcPath = "F:\\Downloads\\TencentFiles\\344041962\\FileRecv\\jd5.log";
        List<String> lines = FileUtils.readLines(new File(srcPath), StandardCharsets.UTF_8);
        String sTag = "msg={";
        int sTagLen = sTag.length();
        int lineNum = 0;
        int parsedCount = 0;
        for (String line : lines) {
            lineNum++;

            int i = line.indexOf(sTag);
            if (i < 0) {
                continue;
            }
            String dataStr = line.substring(i + sTagLen, line.length() - 2);
            byte[] bytes = ByteUtil.hexStr2BytesWithoutSpace(dataStr);
            parsedCount++;
            System.out.println((lineNum) + "=========Start");
            System.out.println(dataStr);
            System.out.println(parsedCount+"——sql string: " + new String(bytes));
            System.out.println(ByteUtil.toHexString(bytes));
            System.out.println((lineNum) + "=========End");

        }
    }

    private static void printHexToString(String hex) throws UnsupportedEncodingException {
        byte[] bytes = ByteUtil.hexStr2Bytes(hex);

        byte[] sqlData = parseRealSqlData(bytes);
        System.out.println(Arrays.toString(sqlData));
        System.out.println(new String(sqlData,"GBK"));
    }

    private static byte[] filterSingleComment(byte[] data) {
        if (ArrayUtils.isEmpty(data)) {
            return null;
        }

        //根据单行注释获取存储SQL长度的下标
        int lenIndex = indexOfSqlLengthBySingleLineComment(data);
        if (lenIndex < 0) {
            //不包含单行注释，直接返回
            return data;
        }

        //从单行注释的后的第一个字节开始，查找换行符(\r)作为单行注释的结尾
        int endIndex = lenIndex + SINGLE_LINE_COMMENT_BYTES.length + 1;
        boolean end = false;
        byte lfByte = (byte) (CharUtils.LF);
        while (endIndex < data.length) {
            byte datum = data[endIndex];
            if (datum == lfByte) {
                break;
            }
            endIndex++;
        }
        return data;
    }
}
