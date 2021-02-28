package com.wind.core.model.enums;

/**
 * @package com.wind.core.model.enums
 * @className IniType
 * @note ini枚举类型
 * @author wind
 * @date 2021/2/28 11:54
 */
public enum IniKeyType {

    /**
     * common
     */
    ON("*", "on"),
    IMPORT("*", "import"),

    /**
     * param
     */
    PARAM_EXTEND_POJO("param", "isExtendPojo"),

    /**
     * jdbc
     */
    JDBC_DRIVER("datasource", "driver"),
    JDBC_URL("datasource", "jdbcUrl"),
    JDBC_USER("datasource", "user"),
    JDBC_PASSWORD("datasource", "password"),

    /**
     * work
     */
    WORK_PATH("work", "path"),
    WORK_CLEAR("work", "isClear"),
    /** isDefault*/
    WORK_DEFAULT("work", "isDefault"),
    WORK_TPL_PATH("work", "tplPath"),
    ;

    private final String type;

    private final String key;

    IniKeyType(String type, String key) {
        this.type = type;
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
