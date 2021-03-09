package com.wind.core.util;

import com.wind.core.callback.DbCallBack;
import com.wind.core.common.BusinessCode;
import com.wind.core.exception.BusinessException;
import com.wind.core.model.JdbcProp;
import com.wind.core.model.db.Column;
import com.wind.core.model.db.PrimaryKey;
import com.wind.core.model.db.Table;
import com.wind.core.model.enums.DriverType;
import com.wind.core.model.enums.MysqlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @package com.wind.core.util
 * @className DbUtil
 * @note db工具类
 * @author wind
 * @date 2020/12/9 19:49
 */
public class DbUtil {

    private final static Logger logger = LoggerFactory.getLogger(DbUtil.class);

    private final JdbcProp jdbcProp;

    private final Connection conn;

    private String defaultCatalog;

    private final Properties props = new Properties();

    public DbUtil(JdbcProp jdbcProp){
        this.jdbcProp = jdbcProp;
        props.setProperty("useInformationSchema", "true");
        props.setProperty("user", jdbcProp.getUser());
        props.setProperty("password", jdbcProp.getPassword());
        this.conn = getConn();
    }

    /**
     * 获取数据库连接
     * @return
     */
    private Connection getConn(){
        if(this.conn != null){
            return this.conn;
        }
        Connection conn = null;
        try {
            Class.forName(jdbcProp.getDriver());
            conn = DriverManager.getConnection(jdbcProp.getJdbcUrl(), props);
            this.defaultCatalog = conn.getCatalog();
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("DbUtil.getConn failed, err is {}", e.getMessage());
            throw new BusinessException(e);
        }
        return conn;
    }



    /**
     * 获取元数据信息
     * @param callBack
     */
    private void exec(DbCallBack callBack){
        Connection con = null;
        try {
            con = getConn();
            DatabaseMetaData db = con.getMetaData();
            callBack.call(db);
        } catch (SQLException e) {
            logger.error("DbUtil.exec failed, err is {}", e.getMessage());
        } finally {
            close(con);
        }
    }

    /**
     * close
     * @param con
     */
    private void close(Connection con){
        if(con != null){
            try {
                con.close();
            } catch (SQLException e) {
                logger.error("DbUtil.close failed, err is {}", e.getMessage());
            }
        }
    }

    /**
     * 获取所有数据库实例
     * @return
     */
    public List<String> getAllDb(){
        List<String> dbs = new ArrayList<>();
        exec(db -> {
            ResultSet rs = db.getCatalogs();
            while(rs.next()){
                String dbName = rs.getString("TABLE_CAT");
                dbs.add(dbName);
            }
            rs.close();
        });
        return dbs;
    }

    /**
     * 获取数据库所有表
     * @param catalog
     * @return
     */
    public List<Table> getTables(String catalog){
        List<Table> tables = new ArrayList<>();
        exec(db -> {
            ResultSet rs = db.getTables(StringUtil.isEmpty(catalog) ? defaultCatalog :
                    catalog, null, null, new String[]{"TABLE"});
            while(rs.next()){
                Table table = getTable(db, rs);
                if(table != null) {
                    tables.add(table);
                }
            }
            rs.close();
        });
        return tables;
    }

    /**
     * 获取数据库表的描述信息
     * @param catalog
     * @param tableName
     * @return
     * @throws Exception
     */
    public List<Table> getTables(String catalog, String tableName) {
        Connection con = null;
        List<Table> tables = new ArrayList<>();
        try {
            con = getConn();
            DatabaseMetaData db = con.getMetaData();
            ResultSet rs = db.getTables(catalog, null, "%" + tableName + "%", new String[]{"TABLE"});
            while(rs.next()) {
                tables.add(getTable(db, rs));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("DbUtil.getTables failed, err is {}", e.getMessage());
        } finally {
            close(con);
        }
        return tables;
    }

    /**
     * 获取数据库表的描述信息
     * @param catalog
     * @param tableName
     * @return
     * @throws Exception
     */
    public Table getTable(String catalog, String tableName) {
        Connection con = null;
        Table table = null;
        try {
            con = getConn();
            DatabaseMetaData db = con.getMetaData();
            ResultSet rs = db.getTables(catalog, null, tableName, new String[]{"TABLE"});
            if(rs.next()) {
                table = getTable(db, rs);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("DbUtil.getTable failed, err is {}", e.getMessage());
        } finally {
            close(con);
        }
        return table;
    }

    /**
     * 组装table数据
     * @param rs
     * @return
     * @throws Exception
     */
    private Table getTable(DatabaseMetaData db, ResultSet rs) {
        Table table = null;
        if(rs != null){
            try {
                table = new Table();
                String tableName = rs.getString("TABLE_NAME");
                table.setName(tableName);
                table.setAlias(StringUtil.getCamelCase(tableName, false));
                table.setProperty(StringUtil.getCamelCase(tableName, true));
                String catalog = rs.getString("TABLE_CAT");
                table.setCatalog(catalog);
                table.setRemarks(rs.getString("REMARKS"));
                List<PrimaryKey> primaryList = getPrimaryKeys(db, catalog, tableName);
                table.setPrimaryList(primaryList);
                List<Column> columnList = getColumns(db, catalog, tableName);
                columnList.forEach(c -> {
                    c.setPrimary(primaryList.stream().anyMatch(p -> c.getName().equals(p.getName())));
                });
                table.setColumnList(columnList);
                if(!primaryList.isEmpty()){
                    PrimaryKey primaryKey = primaryList.get(0);
                    table.setPrimary(columnList.stream().filter(c -> primaryKey.getName().equals(c.getName())).findFirst().orElse(null));
                }

            } catch (SQLException e) {
                logger.error("DbUtil.getTable failed, err is {}", e.getMessage());
            }
        }
        return table;
    }

    /**
     * 获取数据库列信息
     * @param catalog
     * @param tableName
     * @return
     */
    public List<Column> getColumns(String catalog, String tableName){
        List<Column> columns = new ArrayList<>();
        exec(db -> {
            try {
                columns.addAll(getColumns(db, catalog, tableName));
            } catch (Exception e) {
                logger.error("DbUtil.getColumns failed, err is {}", e.getMessage());
            }
        });
        return columns;
    }

    /**
     * 组装column数据
     * @param db
     * @param catalog
     * @param tableName
     * @return
     * @throws SQLException
     * @throws Exception
     */
    private List<Column> getColumns(DatabaseMetaData db, String catalog, String tableName) throws SQLException{
        List<Column> columns = new ArrayList<>();
        ResultSet rs = db.getColumns(catalog, "%", tableName, "%");
        String driver = jdbcProp.getDriver();
        while (rs.next()){
            Column column = new Column();
            String colName = rs.getString("COLUMN_NAME");
            String typeName = rs.getString("TYPE_NAME");
            column.setName(colName);
            column.setType(typeName);
            String javaType =getJavaType(typeName, driver);
            column.setJavaType(javaType);
            String[] typeArr = javaType.split("\\.");
            if(typeArr.length > 0){
                column.setClassType(typeArr[typeArr.length - 1]);
            }
            column.setAlias(StringUtil.getCamelCase(colName, false));
            column.setProperty(StringUtil.getCamelCase(colName, true));
            column.setColumnSize(rs.getInt("COLUMN_SIZE"));
            column.setNullable(rs.getInt("NULLABLE"));
            column.setRemarks(rs.getString("REMARKS"));
            column.setDigits(rs.getInt("DECIMAL_DIGITS"));
            columns.add(column);
        }
        rs.close();
        return columns;
    }

    /**
     * 获取java type
     * @param dbType
     * @param driver
     * @return
     */
    private String getJavaType(String dbType, String driver){
        if(DriverType.MYSQL.equalType(driver)){
            return MysqlType.getType(dbType);
        }else if(DriverType.MYSQL8.equalType(driver)){
            return MysqlType.getType(dbType);
        }
        throw new BusinessException(BusinessCode.THE_DRIVER_NOT_SUPPORT.getDesc());
    }

    /**
     * TABLE_CAT String => 表类别（可为 null）
     * TABLE_SCHEMA String => 表模式（可为 null）
     * TABLE_NAME String => 表名称
     * COLUMN_NAME String => 列名称
     * KEY_SEQ short => 主键中的序列号
     * PK_NAME String => 主键的名称（可为 null）
     * @param catalog
     * @param tableName
     * @return
     */
    public List<PrimaryKey> getPrimaryKeys(String catalog, String tableName){
        List<PrimaryKey> keys = new ArrayList<>();
        exec(db -> keys.addAll(getPrimaryKeys(db, catalog, tableName)));
        return keys;
    }

    /**
     * 组装primaryKey数据
     * @param db
     * @param catalog
     * @param tableName
     * @return
     */
    private List<PrimaryKey> getPrimaryKeys(DatabaseMetaData db, String catalog, String tableName){
        List<PrimaryKey> keys = new ArrayList<>();
        try {
            ResultSet rs = db.getPrimaryKeys(catalog, null, tableName);
            while(rs.next()) {
                PrimaryKey primaryKey = new PrimaryKey();
                primaryKey.setName(rs.getString("COLUMN_NAME"));
                primaryKey.setPkName(rs.getString("PK_NAME"));
                primaryKey.setKeySeq(rs.getShort("KEY_SEQ"));
                keys.add(primaryKey);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("DbUtil.getPrimaryKeys failed, err is {}", e.getMessage());
        }
        return keys;
    }
}
