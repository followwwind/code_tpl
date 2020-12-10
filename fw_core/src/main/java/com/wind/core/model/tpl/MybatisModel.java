package com.wind.core.model.tpl;

import com.wind.core.model.db.Table;

/**
 * @package com.wind.core.model.ftl
 * @className MybatisModel
 * @note mybatis模型
 * @author wind
 * @date 2020/12/9 21:53
 */
public class MybatisModel extends Table{

    /**
     * mybatis对应命名空间
     */
    private String namespace;

    /**
     * mybatis对应实体类全名
     */
    private String type;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
