package com.wind.ui.code;

import com.wind.core.model.JdbcProp;
import com.wind.core.model.db.Table;
import com.wind.core.util.DbUtil;
import com.wind.core.util.ftl.FtlObj;
import com.wind.core.util.ftl.FtlUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenTest {

    Map<String, Object> params = new HashMap<>();

    @Before
    public void init(){
        params.put("isPojo", true);
        params.put("isRest", false);
        params.put("isSwagger", true);
        params.put("bootName", "com.wind.boot");
    }

    public DbUtil getDbUtil(){
        JdbcProp jdbcProp = new JdbcProp();
        jdbcProp.setDriver("com.mysql.jdbc.Driver");
        jdbcProp.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull");
        jdbcProp.setUser("root");
        jdbcProp.setPassword("123456");
        return new DbUtil(jdbcProp);
    }

    @Test
    public void testCode(){
        DbUtil dbUtil = getDbUtil();
        List<Table> tableList = dbUtil.getTables("test");
        tableList.forEach(t -> {
            t.addParam(params);
            genController(t);
            genService(t);
            genMapper(t);
            genModel(t);
            genAngularJs(t);
        });
    }

    public void genController(Table table){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/spring");
        freeMarker.setCfgName("controller.ftl");
        table.addImport("com.wind.boot.common.message.JsonResult");
        //System.out.println(JSON.toJSON(table));
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl\\controller");
        freeMarker.setFileName(table.getProperty() + "Controller.java");
        FtlUtil.genCode(freeMarker);
    }

    public void genService(Table table){
        table.initImport();
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/spring");
        freeMarker.setCfgName("service.ftl");
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl\\service");
        freeMarker.setFileName(table.getProperty() + "Service.java");
        FtlUtil.genCode(freeMarker);

        table.initImport();
        table.addImport("com.wind.boot.common.message.JsonResult");
        table.addImport("com.baidu.unbiz.fluentvalidator.annotation.FluentValid");
        table.addImport("com.wind.boot.config.validation.group.Add");
        table.addImport("com.wind.boot.config.validation.group.Update");
        table.addImport("com.wind.boot.config.message.HttpCode");
        table.addImport("com.wind.boot.config.persistence.Page");
        table.addImport("com.wind.boot.util.BeanUtil");
        freeMarker.setCfgName("serviceImpl.ftl");
        freeMarker.setFileName(table.getProperty() + "ServiceImpl.java");
        FtlUtil.genCode(freeMarker);
    }

    public void genMapper(Table table){
        table.initImport();
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/mybatis");
        freeMarker.setCfgName("dao.ftl");
        table.addImport("com.wind.boot.config.persistence.annotation.SqlMapper");
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl\\dao");
        freeMarker.setFileName(table.getProperty() + "Mapper.java");
        FtlUtil.genCode(freeMarker);

        table.initImport();
        freeMarker.setCfgName("mapper.ftl");
        freeMarker.setFileName(table.getProperty() + "Mapper.xml");
        FtlUtil.genCode(freeMarker);
    }

    public void genModel(Table table){
        table.initImport();
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/model");
        freeMarker.setCfgName("po.ftl");
        table.initImport();
        freeMarker.setData(table);
        table.addImport("com.wind.boot.config.persistence.BasePO");
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl\\entity\\po");
        freeMarker.setFileName(table.getProperty() + ".java");
        FtlUtil.genCode(freeMarker);

        table.initImport();
        table.addImport("com.wind.boot.config.persistence.BaseDTO");
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl\\entity\\dto");
        freeMarker.setCfgName("search.ftl");
        freeMarker.setFileName(table.getProperty() + "SearchDTO.java");
        FtlUtil.genCode(freeMarker);

        table.initImport();
        table.addImport("com.wind.boot.config.persistence.BaseDTO");
        freeMarker.setCfgName("dto.ftl");
        freeMarker.setFileName(table.getProperty() + "DTO.java");
        FtlUtil.genCode(freeMarker);

        table.initImport();
        table.addImport("com.wind.boot.config.persistence.BaseVO");
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl\\entity\\vo");
        freeMarker.setCfgName("vo.ftl");
        freeMarker.setFileName(table.getProperty() + "VO.java");
        FtlUtil.genCode(freeMarker);
    }

    public void genAngularJs(Table table){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgDir("src/main/resources/ftl/java/angularjs");
        freeMarker.setCfgName("html.ftl");
        table.initImport();
        freeMarker.setData(table);
        freeMarker.setFileDir("C:\\Users\\follow\\Desktop\\tpl\\angularjs");
        freeMarker.setFileName(table.getAlias() + ".html");
        FtlUtil.genCode(freeMarker);
        freeMarker.setCfgName("js.ftl");
        freeMarker.setFileName(table.getAlias() + ".js");
        FtlUtil.genCode(freeMarker);
    }
}
