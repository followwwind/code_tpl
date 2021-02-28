package com.wind.ui.mybatis;

import com.wind.core.model.JdbcProp;
import com.wind.core.model.db.Table;
import com.wind.core.util.DbUtil;
import com.wind.core.util.ftl.FtlObj;
import com.wind.ui.util.FtlUtil;
import org.junit.Test;

public class MybatisTest {

    public DbUtil getDbUtil(){
        JdbcProp jdbcProp = new JdbcProp();
        jdbcProp.setDriver("com.mysql.jdbc.Driver");
        jdbcProp.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/code?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull");
        jdbcProp.setUser("root");
        jdbcProp.setPassword("123456");
        return new DbUtil(jdbcProp);
    }

    @Test
    public void genMapperTest(){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/mybatis");
        freeMarker.setCfgName("dao.ftl");
        DbUtil dbUtil = getDbUtil();
        Table table = dbUtil.getTable("test", "user");
//        Table table = dbUtil.getTable("test", "test");
//        table.addParam("isPojo", false);
//        System.out.println(JSON.toJSON(table));
        table.addParam("bootName", "com.wind.boot");
        table.addImport("com.wind.boot.common.persistence.annotation.SqlMapper");
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl");
        freeMarker.setFileName(table.getAlias() + "Mapper.java");
        FtlUtil.genCode(freeMarker, true);
        freeMarker.setCfgName("mapper.ftl");
        freeMarker.setFileName(table.getAlias() + "Mapper.xml");
        FtlUtil.genCode(freeMarker, true);
    }
}
