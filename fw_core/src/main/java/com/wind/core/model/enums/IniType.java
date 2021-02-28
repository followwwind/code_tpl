package com.wind.core.model.enums;

/**
 * @package com.wind.core.model.enums
 * @className IniType
 * @note ini枚举类型
 * @author wind
 * @date 2021/2/28 11:54
 */
public enum IniType {

    /**
     * datasource
     */
    DATASOURCE("datasource"),

    /**
     * work
     */
    WORK("work"),

    /**
     * work
     */
    PARAM("param"),

    CONTROLLER("controller"),

    DAO("dao"),

    PO("po"),

    DTO("dto"),

    SEARCH_DTO("searchDto"),

    VO("vo"),

    SERVICE("service"),

    SERVICE_IMPL("serviceImpl"),


    ;
    private final String type;


    IniType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
