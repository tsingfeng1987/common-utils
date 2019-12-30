package com.qing.common;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author guoqf
 * @date 2019/12/16 15:29
 */

public class SqlConcatTest {
    private static final Logger logger = Logger.getLogger(SqlConcatTest.class.getName());
    private static boolean isMysqlDataBase = true;

    public static void main(String[] args) {
//
//        getFileSqlForRegex(Arrays.asList(1000L, 1111L, 2222L), "10000", "cat test.txt", "0", "1000");
//
//        getFileSql(Arrays.asList(1000L, 1111L, 2222L), "10000", "0", "1000");

//        String extSql = getMetaFileExtSql(Arrays.asList(10000L, 10001L, 10002L));
        String extSql = getMetaFileExtSql(Collections.singletonList(10000L));
        System.out.println("extSql: " + extSql);
    }


    public static String getFileSql(List<Long> metaIds, String entityId, String status, String mainAcctId) {


        StringBuilder idBuilder = new StringBuilder();
        for (int i = 0; i < metaIds.size(); i++) {
            Long id = metaIds.get(i);
            if (id == null) {
                continue;
            }
            if (i > 0) {
                idBuilder.append(",");
            }
            idBuilder.append(id);
        }
        if (idBuilder.length() == 0) {
            return null;
        }

        String sdate = getSysdate();
        StringBuilder str = new StringBuilder("select distinct r1.*,t4.policy_id,t4.control_style,t4.appr_type as appr_type1 , " +
                " t4.OPERATOR_ID, t4.APP_RELATION_TYPE " +
                " from (select t.*,t2.busi_scene_id busi_scene_id,t2.scene_name scene_name from UAP_META_FILE t," +
                "UAP_SYS_SCENE t2,UAP_SYS_SCENE_DATA t3 " +
                "where t.meta_data_id = t3.meta_data_id " +
                "and t2.busi_scene_id = t3.sys_scene_id " +
                "and t3.sys_type = '2' ");
        if ("0".equals(status)) {
            str.append("and t.status = '").append(status).append("' ").append("and t2.status = '").append(status).append("' ");
        } else {
            str.append("and (t.status = '").append(status).append("' ").append("or t2.status = '").append(status).append("' ) ");
        }

        str.append(" and t.meta_data_id in (").append(idBuilder.toString()).append(") ");

        if (!"".equals(sdate)) {
            str.append(" and not exists (select 1 from uap_gold_bank_appoint a where a.appoint_type= 1 and a.busy_type ='2' " + "and a.operate_value = t.meta_data_id and a.system_id= ").append(entityId).append(" and a.operate_main_acct_id = '").append(mainAcctId).append("' ");

            if (isMysqlDataBase) {
                str.append(" and sysdate() between a.appoint_begin_time and a.appoint_end_time)");
            } else {
                str.append("and to_date('").append(sdate).append("','yyyy-MM-dd HH24:mi:ss') between a.appoint_begin_time and a.appoint_end_time)");
            }
        }
        str.append(") r1 LEFT JOIN UAP_BANK_CONTROL_POLICY t4 ON r1.meta_data_id = t4.busi_id and t4.busi_type = '2' and t4.status ='0' ");

        String res = str.toString();
        logger.info("主机金库查询策略sql:" + res);
        return res;
    }

    private static String getSysdate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        return df.format(new Date());
    }

    public static String getFileSqlForRegex(List<Long> metaIds, String entityId, String bizOpCmd, String status, String mainAcctId) {



        StringBuilder idBuilder = new StringBuilder();

        for (int i = 0; i < metaIds.size(); i++) {
            Long id = metaIds.get(i);
            if (id == null) {
                continue;
            }
            if (i > 0) {
                idBuilder.append(",");
            }
            idBuilder.append(id);
        }
        if (idBuilder.length() == 0) {
            return null;
        }

        bizOpCmd = bizOpCmd.replaceAll("[\\r\\n]", " "); // 去除换行回车，否则正则匹配不准

        String sdate = getSysdate();
        StringBuilder str = new StringBuilder("select distinct r1.*,t4.policy_id,t4.control_style,t4.appr_type as appr_type1 , " +
                " t4.OPERATOR_ID, t4.APP_RELATION_TYPE " +
                " from (select t.*,t2.busi_scene_id busi_scene_id,t2.scene_name scene_name from UAP_META_FILE t," +
                "UAP_SYS_SCENE t2,UAP_SYS_SCENE_DATA t3 " +
                "where t.meta_data_id = t3.meta_data_id " +
                "and t2.busi_scene_id = t3.sys_scene_id " +
                "and t3.sys_type = '2' ");

        if (isMysqlDataBase) {
            str.append(" and (lower('").append(bizOpCmd).append("') REGEXP regex OR upper('").append(bizOpCmd).append("') REGEXP regex or '").append(bizOpCmd).append("' REGEXP regex) ");  // SQL 匹配正则表达式
        } else {
            str.append(" and (regexp_like(upper('").append(bizOpCmd).append("'),REGEX) or regexp_like(lower('").append(bizOpCmd).append("'),REGEX) or regexp_like('").append(bizOpCmd).append("',REGEX)) ");  // SQL 匹配正则表达式
        }
        if ("0".equals(status)) {
            str.append("and t.status = '").append(status).append("' ").append("and t2.status = '").append(status).append("' ");
        } else {
            str.append("and (t.status = '").append(status).append("' ").append("or t2.status = '").append(status).append("' ) ");
        }

        str.append(" and t.meta_data_id in (").append(idBuilder.toString()).append(") ");

        if (!"".equals(sdate)) {
            str.append(" and not exists (select 1 from uap_gold_bank_appoint a where a.appoint_type= 1 and a.busy_type ='2' " + "and a.operate_value = t.meta_data_id and a.system_id= ").append(entityId).append(" and a.operate_main_acct_id = '").append(mainAcctId).append("' ");

            if (isMysqlDataBase) {
                str.append(" and sysdate() between a.appoint_begin_time and a.appoint_end_time)");
            } else {
                str.append("and to_date('").append(sdate).append("','yyyy-MM-dd HH24:mi:ss') between a.appoint_begin_time and a.appoint_end_time)");
            }
        }
        str.append(") r1 LEFT JOIN UAP_BANK_CONTROL_POLICY t4 ON r1.meta_data_id = t4.busi_id and t4.busi_type = '2' and t4.status ='0' ");

        logger.info(" 主机模糊匹配查询sql:" + str);
        return str.toString();
    }

    private static String getMetaFileExtSql(List<Long> hostIds) {

        String sqlTemp = "select distinct META_DATA_ID from UAP_META_FILE_EXT  where HOST_ID ";
        int size = hostIds.size();
        StringBuilder sqlBuilder = new StringBuilder(sqlTemp.length() + size * 21 + 10);
        sqlBuilder.append(sqlTemp);
        if (size == 1) {
            sqlBuilder.append(" = ").append(hostIds.get(0));
        } else {
            StringBuilder idBuilder = new StringBuilder(size * 21);
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    idBuilder.append(",");
                }
                idBuilder.append(hostIds.get(i));
            }
            sqlBuilder.append(" in (").append(idBuilder.toString()).append(")");
        }

        return sqlBuilder.toString();
    }
}
