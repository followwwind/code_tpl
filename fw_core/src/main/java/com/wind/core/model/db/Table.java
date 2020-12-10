package com.wind.core.model.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @package com.wind.core.model.db
 * @className Table
 * @note 表模型
 * @author wind
 * @date 2020/12/9 19:54
 */
public class Table{

    /**
     * 数据库实例名称
     */
    private String catalog;

    /**
     * 表名称
     */
    private String name;

    /**
     * 别名
     */
    private String alias;

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

    private Map<String, Object> param;

    public Table() {
        this.param = new HashMap<>(16);
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

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    /**
     * 添加param
     * @param key
     * @param value
     */
    public void addParam(String key, String value){
        this.param.put(key, value);
    }
}
