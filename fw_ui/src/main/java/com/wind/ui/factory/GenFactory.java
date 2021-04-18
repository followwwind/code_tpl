package com.wind.ui.factory;

import com.wind.core.model.JdbcProp;
import com.wind.core.model.db.Table;
import com.wind.core.model.enums.IniKeyType;
import com.wind.core.model.enums.IniType;
import com.wind.core.model.enums.ParamType;
import com.wind.core.util.DbUtil;
import com.wind.core.util.StringUtil;
import com.wind.core.util.ftl.FtlObj;
import com.wind.ui.util.FtlUtil;
import com.wind.core.util.ini.ParseIni;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @package com.wind.ui.util
 * @className GenUtil
 * @note 代码生成工具
 * @author wind
 * @date 2021/2/28 12:07
 */
public class GenFactory {

    private final Map<String, Object> PARAMS = new HashMap<>();

    private ParseIni ini;

    private GenFactory tool;

    private String workspace = "C:/Users/follow/Desktop/tpl/";

    private static String PATH_STR = "/";
    private static String TRUE = "true";
    private static String FALSE = "false";
    private static String FTL = ".ftl";

    /**
     * 初始化
     * @param ini
     * @throws IOException
     */
    public static GenFactory getInstance(ParseIni ini) throws IOException {
        GenFactory tool = new GenFactory();
        tool.ini = ini;
        String workspace = ini.get(IniKeyType.WORK_PATH);
        if(workspace != null && !workspace.endsWith(PATH_STR)){
            tool.workspace = workspace + PATH_STR;
        }
        tool.PARAMS.putAll(ParamType.initParam());
        Properties paramMap = ini.getSection(IniType.PARAM.getType());
        if(paramMap != null){
            paramMap.forEach((k, v) -> {
                Object val = v.toString();
                if(TRUE.equals(val) || FALSE.equals(val)){
                    val = Boolean.valueOf(v.toString());
                }
                tool.PARAMS.put(k.toString(), val);
            });
        }
        return tool;
    }

    /**
     * 获取db工具类实例
     * @return
     */
    private DbUtil getDbUtil(){
        JdbcProp jdbcProp = new JdbcProp();
        jdbcProp.setDriver(ini.get(IniKeyType.JDBC_DRIVER));
        jdbcProp.setJdbcUrl(ini.get(IniKeyType.JDBC_URL));
        jdbcProp.setUser(ini.get(IniKeyType.JDBC_USER));
        jdbcProp.setPassword(ini.get(IniKeyType.JDBC_PASSWORD));
        return new DbUtil(jdbcProp);
    }

    /**
     * 生成代码
     */
    public void genCode(){
        if(TRUE.equals(ini.get(IniKeyType.WORK_CLEAR))){
            FtlUtil.clear(workspace);
        }
        DbUtil dbUtil = getDbUtil();
        List<Table> tableList = dbUtil.getTables(null);
        if(TRUE.equals(ini.get(IniKeyType.WORK_DEFAULT))){
            tableList.forEach(t -> {
                t.addParam(PARAMS);
                genController(t);
                genService(t);
                genMapper(t);
                genModel(t);
                genAngularJs(t);
                genJdbc(t);
            });
            genJdbcUtil();
        }
        genTpl(tableList);
    }

    private void genTpl(List<Table> tableList){
        String tplPath = ini.get(IniKeyType.WORK_TPL_PATH);
        if(StringUtil.isEmpty(tplPath)){
            return;
        }
        try {
            File file = new File(tplPath);
            File[] fileList = file.listFiles();
            if(fileList == null){
                return;
            }
            tableList.forEach(t -> {
                Stream.of(fileList).filter(f -> f.getName().endsWith(FTL)).forEach(f -> {
                    String name = f.getName();
                    String ftlName = name.replace(FTL, "");
                    String suffix = ftlName;
                    String prefix = "";
                    String[] nameArr = ftlName.split("_");
                    if(nameArr.length == 2){
                        suffix = nameArr[1];
                        prefix = nameArr[0];
                    }
                    FtlObj freeMarker = new FtlObj();
                    freeMarker.setCfgDir(tplPath);
                    freeMarker.setCfgName(name);
                    freeMarker.setData(t);
                    freeMarker.setFileDir(workspace + ftlName);
                    String property = Character.isUpperCase(suffix.charAt(0)) ? t.getProperty() :
                            t.getAlias();
                    freeMarker.setFileName(property + prefix + "." + suffix.toLowerCase());
                    FtlUtil.genCode(freeMarker, true);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genController(Table table){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgName("java/spring/controller.ftl");
        table.addImport(ini.getSectionImport("controller"));
        freeMarker.setData(table);
        freeMarker.setFileDir(workspace + "controller");
        freeMarker.setFileName(table.getProperty() + "Controller.java");
        FtlUtil.genCode(freeMarker, false);
    }

    private void genService(Table table){
        table.clearImport();
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgName("java/spring/service.ftl");
        freeMarker.setData(table);
        freeMarker.setFileDir(workspace + "service");
        freeMarker.setFileName(table.getProperty() + "Service.java");
        FtlUtil.genCode(freeMarker, false);

        table.clearImport();
        freeMarker.setFileDir(workspace + "service/impl");
        table.addImport(ini.getSectionImport("serviceImpl"));
        freeMarker.setCfgName("java/spring/serviceImpl.ftl");
        freeMarker.setFileName(table.getProperty() + "ServiceImpl.java");
        FtlUtil.genCode(freeMarker, false);
    }

    private void genMapper(Table table){
        table.clearImport();
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgName("java/mybatis/dao.ftl");
        freeMarker.setData(table);
        table.addImport(ini.getSectionImport("dao"));
        freeMarker.setFileDir(workspace + "dao");
        freeMarker.setFileName(table.getProperty() + "Mapper.java");
        FtlUtil.genCode(freeMarker, false);

        table.clearImport();
        freeMarker.setFileDir(workspace + "xml");
        freeMarker.setCfgName("java/mybatis/mapper.ftl");
        freeMarker.setFileName(table.getProperty() + "Mapper.xml");
        FtlUtil.genCode(freeMarker, false);
    }

    private void genModel(Table table){
        table.initImport();
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgName("java/model/po.ftl");
        table.initImport();
        freeMarker.setData(table);
        if(TRUE.equals(ini.get(IniKeyType.PARAM_EXTEND_POJO))){
            table.addImport(ini.getSectionImport("po"));
        }
        freeMarker.setFileDir(workspace + "entity/po");
        freeMarker.setFileName(table.getProperty() + ".java");
        FtlUtil.genCode(freeMarker, false);

        table.initImport();
        table.addImport(ini.getSectionImport("searchDto"));
        freeMarker.setFileDir(workspace + "entity/dto");
        freeMarker.setCfgName("java/model/search.ftl");
        freeMarker.setFileName(table.getProperty() + "SearchDTO.java");
        FtlUtil.genCode(freeMarker, false);

        table.initImport();
        if(TRUE.equals(ini.get(IniKeyType.PARAM_EXTEND_POJO))){
            table.addImport(ini.getSectionImport("dto"));
        }
        freeMarker.setCfgName("java/model/dto.ftl");
        freeMarker.setFileName(table.getProperty() + "DTO.java");
        FtlUtil.genCode(freeMarker, false);

        table.initImport();
        freeMarker.setFileDir(workspace + "entity/vo");
        if(TRUE.equals(ini.get(IniKeyType.PARAM_EXTEND_POJO))){
            table.addImport(ini.getSectionImport("vo"));
        }
        freeMarker.setCfgName("java/model/vo.ftl");
        freeMarker.setFileName(table.getProperty() + "VO.java");
        FtlUtil.genCode(freeMarker, false);

        table.initImport();
        freeMarker.setFileDir(workspace + "model");
        freeMarker.setCfgName("java/model/model.ftl");
        freeMarker.setFileName(table.getProperty() + ".java");
        FtlUtil.genCode(freeMarker, false);
    }

    private void genAngularJs(Table table){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgName("java/angular/html.ftl");
        table.clearImport();
        freeMarker.setData(table);
        freeMarker.setFileDir(workspace + "angular/html");
        freeMarker.setFileName(table.getAlias() + ".html");
        FtlUtil.genCode(freeMarker, false);
        freeMarker.setFileDir(workspace + "angular/js");
        freeMarker.setCfgName("java/angular/js.ftl");
        freeMarker.setFileName(table.getAlias() + ".js");
        FtlUtil.genCode(freeMarker, false);
    }

    private void genJdbc(Table table){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setCfgName("java/jdbc/dao.ftl");
        table.clearImport();
        freeMarker.setData(table);
        freeMarker.setFileDir(workspace + "jdbc/dao");
        freeMarker.setFileName(table.getProperty() + "Dao.java");
        FtlUtil.genCode(freeMarker, false);
    }

    private void genJdbcUtil(){
        FtlObj freeMarker = new FtlObj();
        freeMarker.setFileDir(workspace + "jdbc/util");
        freeMarker.setCfgName("java/jdbc/dbUtil.ftl");
        freeMarker.setFileName("DbUtil.java");
        FtlUtil.genCode(freeMarker, false);
        freeMarker.setCfgName("java/jdbc/page.ftl");
        freeMarker.setFileName("Page.java");
        FtlUtil.genCode(freeMarker, false);
    }
}
