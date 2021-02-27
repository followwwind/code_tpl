package com.wind.core.model.enums;

import java.util.stream.Stream;

/**
 * @package com.wind.core.model.enums
 * @className MysqlType
 * @note mysql枚举数据类型
 * @author wind
 * @date 2020/12/9 20:45
 */
public enum MysqlType {

    /**
     * 数值类型
     */
    TINYINT("TINYINT", "java.lang.Integer"),
    SMALLINT("SMALLINT", "java.lang.Integer"),
    MEDIUMINT("MEDIUMINT", "java.lang.Integer"),
    INT("INT", "java.lang.Integer"),
    BIGINT("BIGINT", "java.lang.Long"),
    FLOAT("FLOAT", "java.lang.Float"),
    DOUBLE("DOUBLE", "java.lang.Double"),
    DECIMAL("DECIMAL", "java.math.BigDecimal"),

    /**
     * 字符串类型
     */
    CHAR("CHAR", "java.lang.String"),
    VARCHAR("VARCHAR", "java.lang.String"),
    TINYTEXT("TINYTEXT", "java.lang.String"),
    TEXT("TEXT", "java.lang.String"),
    MEDIUMTEXT("MEDIUMTEXT", "java.lang.String"),
    LONGTEXT("LONGTEXT", "java.lang.String"),

    /**
     * 二进制
     */
    TINYBLOB("TINYBLOB", "byte[]"),
    BLOB("BLOB", "byte[]"),
    MEDIUMBLOB("MEDIUMBLOB", "byte[]"),
    LONGBLOB("LONGBLOB", "byte[]"),
    BINARY("BINARY", "byte[]"),
    VARBINARY("VARBINARY", "byte[]"),
    /**
     * bool类型
     */
    BIT("BIT", "java.lang.Boolean"),

    /**
     * 时间类型
     */
    DATE("DATE", "java.util.Date"),
    TIME("TIME", "java.util.Date"),
    YEAR("YEAR", "java.util.Date"),
    DATETIME("DATETIME", "java.util.Date"),
    TIMESTAMP("TIMESTAMP", "java.util.Date")
    ;

    /**
     * mysql字段类型名称
     */
    private final String name;
    /**
     * 对应java类型
     */
    private final String type;




    MysqlType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    /**
     * 表字段类型转换成java类型
     * @param columnType
     * @return
     */
    public static String getType(String columnType){
        MysqlType opt = Stream.of(MysqlType.values()).
                filter(val -> {
                    String name = val.name;
                    return columnType.replaceAll(" UNSIGNED", "").equals(name);
                }).findFirst().orElse(null);
        return opt != null ? opt.getType() : null;
    }
}
