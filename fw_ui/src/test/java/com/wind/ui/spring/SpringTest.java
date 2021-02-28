package com.wind.ui.spring;

import com.wind.core.model.JdbcProp;
import com.wind.core.model.db.Table;
import com.wind.core.util.DbUtil;
import com.wind.core.util.ftl.FtlObj;
import com.wind.ui.util.FtlUtil;
import org.junit.Test;

public class SpringTest {

    public DbUtil getDbUtil(){
        JdbcProp jdbcProp = new JdbcProp();
        jdbcProp.setDriver("com.mysql.jdbc.Driver");
        jdbcProp.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/code?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull");
        jdbcProp.setUser("root");
        jdbcProp.setPassword("123456");
        return new DbUtil(jdbcProp);
    }

    @Test
    public void genControllerTest(){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/spring");
        freeMarker.setCfgName("controller.ftl");
        DbUtil dbUtil = getDbUtil();
        Table table = dbUtil.getTable("test", "user");
//        Table table = dbUtil.getTable("test", "test");
        //table.addParam("isRest", true);
//        table.addParam("isSwagger", true);
        table.addParam("isPojo", true);
        table.addParam("bootName", "com.wind.boot");
        table.addImport("com.wind.boot.common.message.JsonResult");
        //System.out.println(JSON.toJSON(table));
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl");
        freeMarker.setFileName(table.getAlias() + "Controller.java");
        FtlUtil.genCode(freeMarker, true);
    }

    @Test
    public void genServiceTest(){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/spring");
        freeMarker.setCfgName("service.ftl");
        DbUtil dbUtil = getDbUtil();
        Table table = dbUtil.getTable("test", "user");
//        Table table = dbUtil.getTable("test", "test");
//        table.addParam("isRest", true);
//        table.addParam("isSwagger", true);
        table.addParam("isPojo", true);
        table.addParam("bootName", "com.wind.boot");
        //System.out.println(JSON.toJSON(table));
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl");
        freeMarker.setFileName(table.getAlias() + "Service.java");
        FtlUtil.genCode(freeMarker, true);
        table.addImport("com.wind.boot.common.message.JsonResult");
        table.addImport("com.baidu.unbiz.fluentvalidator.annotation.FluentValid");
        table.addImport("com.wind.boot.common.validation.group.Add");
        table.addImport("com.wind.boot.common.validation.group.Update");
        table.addImport("com.wind.boot.util.BeanUtil");
        table.addImport("com.wind.boot.common.message.HttpCode");
        table.addImport("com.wind.boot.common.persistence.Page");
        freeMarker.setCfgName("serviceImpl.ftl");
        freeMarker.setFileName(table.getAlias() + "ServiceImpl.java");
        FtlUtil.genCode(freeMarker, true);
    }
}
