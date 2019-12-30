package com.qing.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {



//        System.out.println( "Hello World!" );
//        String reg = "DW_MEMBER_WAP_.*";
//        System.out.println("DW_MEMBER_WAP_)".matches(reg));




//        int[] arr = {1, 2, 2, 3};
//        HashSet<Integer> hashSet = new HashSet<Integer>();
//        for (int ar:arr
//             ) {
//            hashSet.add(ar);
//        }
//        System.out.println(hashSet);
/*
        int[] arr = {1, 2, 3, 3, 3, 5, 9};
        int target = 3;
        App app = new App();
        System.out.println(app.binarySearchMinIndex(arr, target));*/

//        testLongArray();
//        convertSingleDataToSql();
//        readDataAndWriteSql();
        readMultiFilesAndWriteSql();

/*        List<Long> longs = Arrays.asList(1L, 2L, 3L, 4L);
        System.out.println("longs: " + longs);
        List<Long> collect = longs.stream().filter(item -> item > 2L).collect(Collectors.toList());
        System.out.println("Collect: "+collect);
        System.out.println("longs: " + longs);*/

    }

    private static void convertSingleDataToSql() {
//        String dataStr = "00 83 00 00 06 00 00 00 00 00 03 3E 1D 03 00 00 00 01 00 00 00 01 60 00 00 00 01 01 20 00 00 00 01 01 40 0B 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 00 00 00 00 20 73 65 6C 65 63 74 20 2A 20 66 72 6F 6D 20 75 73 65 72 69 6E 66 6F 2E 63 75 73 74 6F 6D 65 72 0A ";
        String dataStr = "00 98 00 00 06 00 00 00 00 00 03 52 02 01 1B 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 1B 00 00 00 01 2D 00 00 00 01 1B 00 00 00 30 11 00 00 01 21 00 00 00 01 24 00 00 00 00 00 00 00 00 00 00 00 00 00 01 23 01 00 00 01 09 48 57 5F 5A 48 41 4F 58 4A 09 5A 57 58 36 30 35 32 38 39 0F 43 48 49 4E 41 5C 5A 57 58 36 30 35 32 38 39 09 7A 57 58 36 30 35 32 38 39 0B 31 34 38 32 30 3A 31 33 34 34 34 0C 70 6C 73 71 6C 64 65 76 2E 65 78 65 ";
//        String dataStr = "73 65 6C 65 63 74 20 2A 20 66 72 6F 6D 20 75 61 70 5F 6D 61 69 6E 5F 61 63 63 74 5F 72 6F 6C 65 0A";
//        String dataStr = "00 61 00 00 06 00 00 00 00 00 11 69 18 01 01 01 01 01 03 5E 19 02 80 61 00 01 01 21 01 01 0D 01 01 00 00 00 00 00 00 01 00 01 01 01 00 00 01 01 21 73 65 6C 65 63 74 20 2A 20 66 72 6F 6D 20 75 61 70 5F 6D 61 69 6E 5F 61 63 63 74 5F 72 6F 6C 65 0A 01 01 00 00 00 00 00 00 01 01 00 00 00 00 00";
        byte[] bytes = ByteUtil.hexStr2Bytes(dataStr);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }


    private static void convertMultiDataToSql() {
        String filePath = "C:\\Users\\tsing\\Desktop\\033edata\\multi_data.log";
        File file = new File(filePath);
        try {
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
//            byte[] bytes = ByteUtil.hexStr2Bytes(dataStr);
//            System.out.println(new String(bytes, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void readMultiFilesAndWriteSql() {
        String dir = "F:\\Downloads\\TencentFiles\\344041962\\FileRecv\\";
//        String dir = "C:\\Users\\tsing\\Desktop\\033edata\\";
        String[] fileNames = {"1.txt", "2.txt", "3.txt", "4.txt"};
        String fileToFound = "1(4).log";
        Iterator<File> txtIt = FileUtils.iterateFiles(new File(dir), new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return fileToFound.equals(file.getName());
            }

            @Override
            public boolean accept(File dir, String name) {
                return fileToFound.equals(name);
            }
        }, null);
        while (txtIt.hasNext()) {
            File next = txtIt.next();
            int lineNum=1;
            try {
                List<String> lines = FileUtils.readLines(next, StandardCharsets.UTF_8);
                for (String line : lines
                ) {
                    handleOneLine(lineNum++, line);
                }
                System.out.println();
                System.out.println(next.getName()+" end : ===============================");
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static void readDataAndWriteSql() {

//        String filePath = "C:\\Users\\tsing\\Desktop\\oracle_client_data1.txt";
        String filePath = "F:\\Downloads\\TencentFiles\\344041962\\FileRecv\\7.txt";
//        String filePath = "F:\\Downloads\\TencentFiles\\344041962\\FileRecv\\8.txt";
        int lineNum=1;
        try {
            List<String> lines = FileUtils.readLines(new File(filePath), StandardCharsets.UTF_8);
            for (String line : lines
                 ) {
                 handleOneLine(lineNum++, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleOneLine(int lineNum, String line) {
        String dataStr;
        String tag = "read client data ==>";
        int tagLength = tag.length();
        int i = line.indexOf(tag);
        if (i < 0) {
            return ;
        }
        dataStr = line.substring(i + tagLength);

        byte[] bytes = ByteUtil.hexStr2Bytes(dataStr);
        System.out.println(lineNum + ":--->");
        System.out.println(new String(bytes, StandardCharsets.UTF_8));

    }

    private static void testLongArray() {
        Long[] ids = {1L, 2L, 3L,2L,3L};

        Long[] longs = Arrays.stream(ids).distinct().toArray(Long[]::new);

        for (Long id :
                longs) {
            System.out.println(id);
        }
    }

    public int binarySearchMinIndex(int[] arr, int target){
        if(ArrayUtils.isEmpty(arr)){
            return -1;
        }

        if(arr.length==1){
            return arr[0] == target ? 0 : -1;
        }

        return binarySearch(arr,target,0, arr.length);
    }

    private int binarySearch(int[] arr, int target, int from, int to){
        if(arr.length==1){
            return arr[0] == target ? 0 : -1;
        }
        int mid = from + (to - from)/2;

        if(arr[mid] == target){
            if(from < mid && arr[mid -1]==target){
                return binarySearch(arr, target, from, mid);
            }
            return mid;
        }else if(arr[mid] > target){
            if(mid>from){
                return binarySearch(arr,target,from,mid);
            }
            return -1;
        }else{
            if(to > mid+1){
                return binarySearch(arr, target,mid+1,to);
            }
            return -1;
        }
    }
}
