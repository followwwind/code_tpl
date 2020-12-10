package com.wind.ui;

import com.alibaba.fastjson.JSON;
import com.wind.core.model.JdbcProp;
import com.wind.core.model.db.Table;
import com.wind.core.util.DbUtil;
import com.wind.core.util.ftl.FreeMarker;
import com.wind.core.util.ftl.FtlUtil;
import org.junit.Test;

public class FtlUtilTest {

    @Test
    public void genTest(){
        FreeMarker freeMarker = new FreeMarker();
        freeMarker.setCfgDir("src/main/resources/ftl/java/mybatis");
        freeMarker.setCfgName("Mapper.ftl");
        JdbcProp jdbcProp = new JdbcProp();
        jdbcProp.setDriver("com.mysql.jdbc.Driver");
        jdbcProp.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull");
        jdbcProp.setUser("root");
        jdbcProp.setPassword("123456");
        DbUtil dbUtil = new DbUtil(jdbcProp);
        Table table = dbUtil.getTable("test", "user");
        System.out.println(JSON.toJSON(table));
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl");
        freeMarker.setFileName("Mapper.java");
        FtlUtil.genCode(freeMarker);
    }
}
