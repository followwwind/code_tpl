package com.wind.core.util.ftl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @package com.wind.core.model
 * @className ftl Param
 * @note 参数
 * @author wind
 * @date 2021/1/28 21:43
 */
public class FtlParam implements Serializable {

    private Map<String, Object> param;

    public FtlParam() {
        param = new HashMap<>(16);
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
    public void addParam(String key, Object value){
        this.param.put(key, value);
    }

    /**
     * 添加param
     * @param params
     */
    public void addParam(Map<String, Object> params){
        this.param.putAll(params);
    }
}
