package com.wind.core.callback;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @package com.wind.core.callback
 * @className DbCallBack
 * @note 数据库元数据回调
 * @author wind
 * @date 2020/12/9 20:04
 */
@FunctionalInterface
public interface DbCallBack {

    /**
     * 数据库元数据操作
     * @param db
     * @throws SQLException
     */
    void call(DatabaseMetaData db) throws SQLException;
}
