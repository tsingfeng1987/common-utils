package com.qing.common.utils.json;

import com.qing.common.utils.SuperTest;
import com.qing.common.utils.model.JsonObj;
import org.junit.Test;

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
            Object o = JacksonUtil.stringToJson(str, JsonObj.class);
            JsonObj jsonObj = (JsonObj) o;
            System.out.println("json= " + jsonObj.getAnInt() + ", " + jsonObj.getAnStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
