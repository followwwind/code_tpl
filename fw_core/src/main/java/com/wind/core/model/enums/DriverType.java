package com.wind.core.model.enums;

/**
 * @package com.wind.core.model.enums
 * @className DriverType
 * @note 驱动类型
 * @author wind
 * @date 2020/12/9 22:38
 */
public enum DriverType {

    /**
     * mysql
     */
    MYSQL("com.mysql.jdbc.Driver"),

    /**
     * mysql8
     */
    MYSQL8("com.mysql.cj.jdbc.Driver"),
    ;
    /**
     * 对应java类型
     */
    private final String type;

    DriverType(String type) {
        this.type = type;
    }

    public boolean equalType(String type){
        return this.type.equals(type);
    }

}
