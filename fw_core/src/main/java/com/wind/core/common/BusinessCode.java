package com.wind.core.common;

/**
 * @package com.wind.core.common
 * @className BusinessCode
 * @note 业务编码异常
 * @author wind
 * @date 2020/12/9 23:41
 */
public enum BusinessCode {

    THE_DRIVER_NOT_SUPPORT("the_driver_not_support", "当前数据库不支持"),
    ;

    private String code;

    private String desc;

    BusinessCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
