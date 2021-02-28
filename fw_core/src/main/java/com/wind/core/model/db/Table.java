package com.wind.core.model.db;

import com.wind.core.util.ftl.FtlParam;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @package com.wind.core.model.db
 * @className Table
 * @note 表模型
 * @author wind
 * @date 2020/12/9 19:54
 */
public class Table extends FtlParam {

    /**
     * 数据库实例名称
     */
    private String catalog;

    /**
     * 表名称
     */
    private String name;

    /**
     * 别名(驼峰命名，首字母小写)
     */
    private String alias;

    /**
     * 别名(驼峰命名，首字母大写)
     */
    private String property;

    /**
     * 表注释
     */
    private String remarks;

    /**
     * 主键
     */
    private List<PrimaryKey> primaryList;
    /**
     * 所有列的信息
     */
    private List<Column> columnList;

    /**
     * java数据类型import
     */
    private List<String> importList;

    /**
     * 第一主键
     */
    private Column primary;

    public Table() {
        super();
        this.importList = new ArrayList<>();
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<PrimaryKey> getPrimaryList() {
        return primaryList;
    }

    public void setPrimaryList(List<PrimaryKey> primaryList) {
        this.primaryList = primaryList;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<String> getImportList() {
        return importList;
    }

    public void setImportList(List<String> importList) {
        this.importList = importList;
    }

    public Column getPrimary() {
        return primary;
    }

    public void setPrimary(Column primary) {
        this.primary = primary;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * 添加import语句
     * @param importStr
     */
    public void addImport(String importStr){
        this.importList.add(importStr);
    }

    /**
     * 添加import语句
     * @param importList
     */
    public void addImport(List<String> importList){
        this.importList.addAll(importList);
    }

    /**
     * clear import
     */
    public void clearImport(){
        this.importList.clear();
    }

    /**
     * 初始化表字段对应的import
     */
    public void initImport(){
        this.importList.clear();
        if(this.columnList == null){
            return;
        }
        addImport(this.columnList.stream().map(Column::getJavaType)
                .filter(r -> !r.contains("java.lang")).distinct().collect(Collectors.toList()));
    }
}
