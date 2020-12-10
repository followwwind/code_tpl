package com.wind.core.model.db;

/**
 * @package com.wind.core.model.db
 * @className Column
 * @note 数据库列模型
 * @author wind
 * @date 2020/12/9 19:51
 */
public class Column {

    /**
     * 列名称
     */
    private String name;

    /**
     * 别名
     */
    private String alias;

    /**
     * 类型
     */
    private String type;

    /**
     * java包全称
     */
    private String javaType;

    /**
     * java类
     */
    private String classType;

    /**
     * 占用字节
     */
    private Integer columnSize;
    /**
     * 是否为空
     * 1.columnNoNulls - 可能不允许使用 NULL 值
     * 2.columnNullable - 明确允许使用 NULL 值
     * 3.columnNullableUnknown - 不知道是否可使用 null
     */
    private Integer nullable;
    /**
     * 小数部分的位数
     */
    private Integer digits;
    /**
     * 描述
     */
    private String remarks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(Integer nullable) {
        this.nullable = nullable;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
}
