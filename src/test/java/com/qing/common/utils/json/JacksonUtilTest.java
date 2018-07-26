package com.qing.common.utils.json;

import com.qing.common.utils.SuperTest;
import com.qing.common.utils.model.JsonObj;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by Guoqingfeng on 2017/10/10.
 */
public class JacksonUtilTest extends SuperTest {


    @Test
    public void testJsonToString() {
        JsonObj jsonObj = new JsonObj();
        jsonObj.setAnInt(1);
        jsonObj.setAnStr("str1");
        try {
            System.out.println("result= " + JacksonUtil.jsonToString(jsonObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStringToJson() {
        String str = "{\"anInt\":11,\"anStr\":\"str11\"}";
        try {
            JsonObj jsonObj = JacksonUtil.stringToJson(str, JsonObj.class);
            System.out.println("json= " + jsonObj.getAnInt() + ", " + jsonObj.getAnStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStringToList() {
        String str = "[{\"anInt\":11,\"anStr\":\"str11\"},{\"anInt\":22,\"anStr\":\"str22\"}]";
        try {
            List<JsonObj> objs = JacksonUtil.stringToList(str, JsonObj.class);
            System.out.println("=============================testStringToList========================");
            for (JsonObj obj :
                    objs) {
                System.out.println("json= " + obj.getAnInt() + ", " + obj.getAnStr());
            }
            System.out.println("=============================testStringToList========================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
