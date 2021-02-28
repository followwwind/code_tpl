package com.wind.ui.jdbc;

import com.alibaba.fastjson.JSON;
import com.wind.core.model.JdbcProp;
import com.wind.core.model.db.Table;
import com.wind.core.util.DbUtil;
import com.wind.core.util.ftl.FtlObj;
import com.wind.ui.util.FtlUtil;
import org.junit.Test;

public class JdbcTest {

    public DbUtil getDbUtil(){
        JdbcProp jdbcProp = new JdbcProp();
        jdbcProp.setDriver("com.mysql.jdbc.Driver");
        jdbcProp.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull");
        jdbcProp.setUser("root");
        jdbcProp.setPassword("123456");
        return new DbUtil(jdbcProp);
    }

    @Test
    public void genModelTest(){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/model");
        freeMarker.setCfgName("model.ftl");
        DbUtil dbUtil = getDbUtil();
//        Table table = dbUtil.getTable("test", "user");
        Table table = dbUtil.getTable("test", "user");
//        table.addParam("isRest", true);
        //System.out.println(JSON.toJSON(table));
        table.initImport();
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl");
        freeMarker.setFileName(table.getProperty() + ".java");
        FtlUtil.genCode(freeMarker);
    }


    @Test
    public void genUtilTest(){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/jdbc");
        freeMarker.setCfgName("dbUtil.ftl");
        freeMarker.setData(null);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl");
        freeMarker.setFileName("DbUtil.java");
        FtlUtil.genCode(freeMarker);
        freeMarker.setCfgName("page.ftl");
        freeMarker.setFileName("Page.java");
        FtlUtil.genCode(freeMarker);
    }

    @Test
    public void genDaoTest(){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/jdbc");
        freeMarker.setCfgName("dao.ftl");
        DbUtil dbUtil = getDbUtil();
//        Table table = dbUtil.getTable("test", "user");
        Table table = dbUtil.getTable("test", "user");
        System.out.println(JSON.toJSONString(table));
//        table.addParam("isRest", true);
        //System.out.println(JSON.toJSON(table));
        table.initImport();
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl");
        freeMarker.setFileName(table.getProperty() + "Dao.java");
        FtlUtil.genCode(freeMarker);
    }
}
