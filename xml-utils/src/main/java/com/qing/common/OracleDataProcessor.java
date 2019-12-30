package com.qing.common;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author guoqf
 * @date 2019/12/24 11:43
 */
public class OracleDataProcessor {

    private static final String SINGLE_LINE_COMMENT_SYMBOL = "--";
    private static final String MULTIPLE_LINE_COMMENT_START_SYMBOL = "/*";
    private static final String MULTIPLE_LINE_COMMENT_END_SYMBOL = "*/";

    private static final byte[] SINGLE_LINE_COMMENT_BYTES = SINGLE_LINE_COMMENT_SYMBOL.getBytes();
    private static final byte[] MULTIPLE_LINE_COMMENT_START_BYTES = MULTIPLE_LINE_COMMENT_START_SYMBOL.getBytes();
    private static final byte[] MULTIPLE_LINE_COMMENT_END_BYTES = MULTIPLE_LINE_COMMENT_END_SYMBOL.getBytes();
    private static final byte BYTE_IN_HEX_0 = 0x00;
    private static final byte BYTE_IN_HEX_1 = 0x01;
    private static final String DB_ORACLE_COMMAND_KEY_WORDS = "CREATE,INSERT,UPDATE,DELETE,SELECT,ALTER,DROP,TRUNCATE,COMMENT,MERGE,DECLARE,BEGIN,GRANT,REVOKE,CALL,EXPLAIN PLAN,LOCK TABLE,RENAME";


    /**
     * 分析数据包data，从中取出过滤掉注释内容的SQL数据包
     *
     * @param data 待分析的数据包
     * @return 过滤掉注释内容的SQL数据包
     */
    public static byte[] processSqlData(byte[] data) {
        //根据多行注释查找长度下标
        return processSqlData(data, true);
    }

    /**
     * 分析数据包data，从中取出执行的SQL数据包，并且可通过设置filterComment为true来将执行的SQL数据包中的注释内容过滤掉
     *
     * @param data          待分析的数据包
     * @param filterComment 是否将执行的SQL数据包中的注释内容过滤掉，true表示需要过滤
     * @return 从data中获取的SQL数据包，如果filterComment为true则将执行的SQL内容中的注释也过滤掉
     */
    public static byte[] processSqlData(byte[] data, boolean filterComment) {
        //获取执行SQL的字节数组
        byte[] processedData = internalProcess(data);

        //取到SQL数据，且需要过去掉注释内容
        if (ArrayUtils.isNotEmpty(processedData) && filterComment) {
            System.out.println("ProcessedData is : " + new String(processedData, StandardCharsets.UTF_8));
            //先过滤多行注释内容
            processedData = filterMultiLineComment(processedData);
            if (ArrayUtils.isNotEmpty(processedData)) {
                System.out.println("After filter multiline comment, ProcessedData is : " + new String(processedData, StandardCharsets.UTF_8));
                //再过滤单行注释内容
                processedData = filterSingleComment(processedData);
                if (ArrayUtils.isNotEmpty(processedData)) {
                    System.out.println("After filter single line comment, ProcessedData is : " + new String(processedData, StandardCharsets.UTF_8));
                }
                return processedData;

            }
        }
        return processedData;
    }

    private static byte[] internalProcess(byte[] data) {
        //根据关键词（SQL命令）查找长度下标
        int lenIndex = indexOfSqlLengthByKeyWords(data);
        if (lenIndex < 0) {
            //根据单行注释查找长度下标
            lenIndex = indexOfSqlLengthBySingleLineComment(data);
            if (lenIndex < 0) {
                //根据多行注释查找长度下标
                lenIndex = indexOfSqlLengthByMultiLineComment(data);
            }
        }
        if (lenIndex >= 0 && lenIndex < data.length) {
            int len = Byte.toUnsignedInt(data[lenIndex]);
            if (len > 0 && lenIndex + len <= data.length) {
                //允许SQL语句后没有数据
                int from = lenIndex + 1;
                return Arrays.copyOfRange(data, from, from + len);
            }
        }

        return null;
    }


    private static int getByteIndexOf(byte[] sources, byte[] src) {
        return getByteIndexOf(sources, src, 0, sources.length - 1);
    }

    private static int getByteIndexOf(byte[] sources, byte[] src, int startIndex, int endIndex) {
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

    private static int indexOfSqlLengthByKeyWords(byte[] sqlData) {
        int startPos;
        String[] keys = DB_ORACLE_COMMAND_KEY_WORDS.split(",");
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


    private static int indexOfSqlLengthByMultiLineComment(byte[] sqlData) {
        return indexOfSqlLength(sqlData, MULTIPLE_LINE_COMMENT_START_BYTES);
    }

    private static int indexOfSqlLengthBySingleLineComment(byte[] sqlData) {
        return indexOfSqlLength(sqlData, SINGLE_LINE_COMMENT_BYTES);
    }

    private static int indexMultiLineCommentStart(byte[] sqlData) {
        return getByteIndexOf(sqlData, MULTIPLE_LINE_COMMENT_START_BYTES);
    }

    private static int indexMultiLineCommentEnd(byte[] sqlData) {
        return getByteIndexOf(sqlData, MULTIPLE_LINE_COMMENT_END_BYTES);
    }

    private static int indexSingleLineComment(byte[] sqlData) {
        return getByteIndexOf(sqlData, SINGLE_LINE_COMMENT_BYTES);
    }


    private static int indexOfSqlLength(byte[] sqlData, byte[] bytesToFound) {
        int startPos = getByteIndexOf(sqlData, bytesToFound, 0, sqlData.length - 1);
        if (startPos > 0) {

            int lenIndex = startPos - 1;
            int len = Byte.toUnsignedInt(sqlData[lenIndex]);


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

        String hex = "01 2A 00 00 06 00 00 00 00 00 11 69 98 01 01 00 00 00 01 00 00 00 03 5E 99 61 80 00 00 00 00 00 00 01 99 00 00 00 01 0D 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 01 01 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 99 73 65 6C 65 63 74 20 2F 2A 41 42 43 44 61 62 63 64 C4 E3 BA C3 B0 A1 21 40 23 24 5F 29 0A 53 44 46 53 44 46 0A 53 44 46 44 53 46 53 44 0A BD F1 CC EC CC EC C6 F8 B2 BB B4 ED CA C7 B7 E7 BA CD C8 D5 C0 F6 B5 C4 0A 2A 2F 0A 2D 2D 2A 20 66 72 6F 6D 20 75 61 70 5F 68 6F 73 74 20 C4 E3 BA C3 0A 2D 2D 4E 49 53 44 46 53 44 41 42 43 B0 B2 B1 A3 0A 2F 2A 0A 31 32 33 32 31 33 B0 A2 B4 EF 0A 2A 2F 0A 20 2A 20 66 72 6F 6D 20 75 61 70 5F 68 6F 73 74 20 C4 E3 BA C3 0A 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
//        String hex = "00 B1 00 00 06 00 00 00 00 00 11 69 2B 01 01 00 00 00 01 00 00 00 03 5E 2C 61 80 00 00 00 00 00 00 01 20 00 00 00 01 0D 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 01 01 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 20 2D 2D D7 A2 CA CD 0A 73 65 6C 65 63 74 20 2A 20 66 72 6F 6D 20 75 61 70 5F 6E 6F 74 69 63 65 0A 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
//        String singleComment = "00 B1 00 00 06 00 00 00 00 00 11 69 2B 01 01 00 00 00 01 00 00 00 03 5E 2C 61 80 00 00 00 00 00 00 01 20 00 00 00 01 0D 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 01 01 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 20 2D 2D D7 A2 CA CD 0A 73 65 6C 65 63 74 20 2A 20 66 72 6F 6D 20 75 61 70 5F 6E 6F 74 69 63 65 0A 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ";
//        printHexToString(hex);

        byte[] sourceData = ByteUtil.hexStr2Bytes(hex);
        printBytesToString(sourceData);
        byte[] parsed = processSqlData(sourceData);
        printBytesToString(parsed);



    }

    private static void printHexToString(String hex) throws UnsupportedEncodingException {
        byte[] bytes = ByteUtil.hexStr2Bytes(hex);

        printBytesToString(bytes);
    }

    private static void printBytesToString(byte[] bytes) throws UnsupportedEncodingException {
        System.out.println(Arrays.toString(bytes));
        System.out.println(new String(bytes, "GBK"));
    }

    private static byte[] filterSingleComment(byte[] data) {
        if (ArrayUtils.isEmpty(data)) {
            return null;
        }

        byte[] filteredData = data;

        //获取单行注释的起始下标
        int startIndex;
        while (filteredData != null && (startIndex = indexSingleLineComment(filteredData)) >= 0) {

            //从单行注释的后的第一个字节开始，查找换行符(\r)作为单行注释的结尾
            int endIndex = startIndex + SINGLE_LINE_COMMENT_BYTES.length + 1;
            byte lfByte = (byte) (CharUtils.LF);
            while (endIndex < filteredData.length) {
                byte datum = filteredData[endIndex];
                if (datum == lfByte) {
                    //发现换行符
                    break;
                }
                endIndex++;
            }


            filteredData = deleteOf(filteredData, startIndex, endIndex + 1);
        }

        return filteredData;
    }


    private static byte[] filterMultiLineComment(byte[] data) {
        if (ArrayUtils.isEmpty(data)) {
            return null;
        }

        byte[] filteredData = data;

        //获取多行注释的起始下标
        int startIndex;
        while ((startIndex = indexMultiLineCommentStart(filteredData)) >= 0) {

            //获取多行注释的结尾下标
            int commentEnd = indexMultiLineCommentEnd(filteredData);

            int endIndex = commentEnd > startIndex ? commentEnd + MULTIPLE_LINE_COMMENT_END_BYTES.length : filteredData.length - 1;

            filteredData = deleteOf(filteredData, startIndex, endIndex + 1);
            if (filteredData == null) {
                return null;
            }
        }

        return filteredData;
    }

    /**
     * 将字节数组data中从起始位置from(包含)到结束位置to(不包含)的(to-from)个字节删除，并返回删除后的字节数组
     *
     * @param data 要删除字节的字节数组
     * @param from 要删除的一段字节的起始位置(包含)
     * @param to   要删除的一段字节的结束位置(不包含)
     * @return 删除指定的一段字节后的字节数组
     */
    private static byte[] deleteOf(byte[] data, int from, int to) {

        if (ArrayUtils.isEmpty(data)) {
            return null;
        }

        if (from < 0) {
            return data;
        }

        if (to <= from) {
            return data;
        }

        if (to > data.length) {
            to = data.length;
        }

        int backwardLen = data.length - to;

        int newDataLen = from + backwardLen;
        if (newDataLen <= 0) {
            //过滤掉单行注释后已经没有数据
            return null;
        }

        byte[] newData = new byte[newDataLen];
        int startPos = 0;
        if (from > 0) {
            System.arraycopy(data, 0, newData, startPos, from);
            startPos += from;
        }

        if (backwardLen > 0) {
            System.arraycopy(data, to, newData, startPos, backwardLen);
        }

        data = newData;
        return data;
    }
}
