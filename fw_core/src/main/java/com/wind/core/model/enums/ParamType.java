package com.wind.core.model.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @package com.wind.core.model.enums
 * @className ParamType
 * @note 参数枚举
 * @author wind
 * @date 2021/2/28 10:07
 */
public enum ParamType {

    /** bootName 业务模块根包名*/
    BOOT_NAME("bootName", "com.wind.boot"),

    /** author */
    AUTHOR("author", "wind"),

    /** isEnglish html，js等是否英文化*/
    IS_ENGLISH("isEnglish", false),

    /** isPojo 是否采用pojo模式 dto，po，vo*/
    IS_POJO("isPojo", true),

    /** isRest 是否采用rest分格，post，put，delete，get*/
    IS_REST("isRest", true),

    /** isSwagger 是否controller加上swagger注解*/
    IS_SWAGGER("isSwagger", false),

    /** isExtendPojo 采用pojo模式 dto，po，vo等模型是否继承相对应的公共模型*/
    IS_EXTEND_POJO("isExtendPojo", false),

    /** isFluentValid 是否采用fluentValid参数校验框架*/
    IS_FLUENT_VALID("isFluentValid", true),

    ;

    private final String name;

    private final Object value;

    ParamType(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    /**
     * 设置参数
     * @param paramMap
     * @param value
     */
    public void setParam(Map<String, Object> paramMap, Object value){
        if(paramMap != null){
            paramMap.put(this.name, value);
        }
    }

    /**
     * 初始化公共参数
     * @return
     */
    public static Map<String, Object> initParam(){
        Map<String, Object> paramMap = new HashMap<>(16);
        Stream.of(ParamType.values()).forEach(p -> {
            paramMap.put(p.name, p.value);
        });
        return paramMap;
    }
}
