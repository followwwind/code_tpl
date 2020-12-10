package com.wind.core.model.db;

/**
 * @package com.wind.core.model.db
 * @className PrimaryKey
 * @note 主键模型
 * @author wind
 * @date 2020/12/9 19:59
 */
public class PrimaryKey {

    /**
     * 列名称
     */
    private String name;
    /**
     * 主键的名称（可为 null）
     */
    private String pkName;
    /**
     * 主键中的序列号
     */
    private short keySeq;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public short getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(short keySeq) {
        this.keySeq = keySeq;
    }
}
