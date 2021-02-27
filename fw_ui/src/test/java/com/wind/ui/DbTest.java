package com.wind.ui;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class DbTest {

    public final static String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false";
    public final static String USER = "root";
    public final static String PWD = "123456";
    public final static String DRIVER = "com.mysql.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行查询语句， ps不赋值回调
     * @param sql
     */
    public <T> List<T> executeQuery(String sql, Class<T> c){
        return executeQuery(sql, null, c);
    }

    /**
     * 执行查询语句， ps不赋值回调
     * @param sql
     */
    public <T> List<T> pageQuery(String sql, Consumer<PreparedStatement> psCall, Class<T> c, int pageNumber, int pageSize){
        String pageSql = String.format("select * from (%s) a limit %d, %d", sql, pageNumber, pageSize);
        return executeQuery(pageSql, psCall, c);
    }

    /**
     * 执行查询语句， ps赋值回调
     * @param sql
     * @param psCall
     */
    public <T> List<T> executeQuery(String sql, Consumer<PreparedStatement> psCall, Class<T> c){
        List<T> list = new ArrayList<>();
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            if(psCall != null){
                psCall.accept(ps);
            }
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int count = md.getColumnCount();
            Map<String, String> colMap = new HashMap<>(16);
            IntStream.range(0, count).forEach(i -> {
                try {
                    String colName = md.getColumnName(i + 1);
                    String camelName = getCamelCase(colName, false);
                    colMap.put(camelName, colName);
                } catch (SQLException e1) {
                    //e1.printStackTrace();
                }
            });
            while (rs.next()){
                list.add(parseTable(rs, c, colMap));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return list;
    }

    /**
     * 解析数据库表对应实体类
     * @param rs
     * @return
     * @throws SQLException
     */
    public <T> T parseTable(ResultSet rs, Class<T> c, Map<String, String> colMap) {
        T entity = null;
        try {
            entity = c.newInstance();
            Field[] fields = c.getDeclaredFields();
            final T tmp = entity;
            Set<String> colList = colMap.keySet();
            //System.out.println(colMap);
            Arrays.stream(fields).filter(f -> colList.contains(f.getName())).forEach(f -> {
                String name = f.getName();
                String colName = colMap.getOrDefault(name, name);
                try {
                    Object val = rs.getObject(colName);
                    if(val != null){
                        f.setAccessible(true);
                        f.set(tmp, val);
                    }
                } catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return entity;
    };

    /**
     * 执行查询记录条数语句， ps赋值回调
     * @param sql
     * @param psCall
     * @param
     */
    public static int count(String sql, Consumer<PreparedStatement> psCall){
        Connection conn = getConnection();
        int count = 0;
        try {
            String countSql = String.format("select count(1) from (%s) a", sql);
            PreparedStatement ps = conn.prepareStatement(countSql);
            if(psCall != null){
                psCall.accept(ps);
            }
            System.out.println(ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs != null && rs.first()){
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return count;
    }

    /**
     * 执行添加， 删除， 更新等操作
     * @param sql
     * @return
     */
    public static int executeUpdate(String sql) {
        Connection conn = getConnection();
        int i = -1;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            i = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return i;
    }

    /**
     * 执行添加， 删除， 更新等操作
     * @param sql
     * @param psCall
     * @return
     */
    public static int executeUpdate(String sql, Consumer<PreparedStatement> psCall){
        Connection conn = getConnection();
        int i = -1;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            if(psCall != null){
                psCall.accept(ps);
            }
            i = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return i;
    }

    /**
     * 获取数据库连接connection
     * @return
     */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL, USER, PWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * @param conn
     */
    public static void close(Connection conn){
        try {
            if(conn != null){
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 驼峰命名 hello_world => HelloWorld
     * @param name 数据库列名
     * @param isFirst 首字母小写为false helloWorld， 大写为true HelloWorld
     * @return
     */
    public static String getCamelCase(String name, boolean isFirst){
        StringBuilder sb = new StringBuilder();
        if(name != null && !"".equals(name)){
            String[] strArr = name.split("_");
            for(int i = 0; i < strArr.length; i++){
                String s = strArr[i];
                if(i == 0){
                    sb.append(getCap(s, isFirst));
                }else{
                    sb.append(getCap(s, true));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将单词首字母变大小写
     * @param str
     * @param flag true变大写， false变小写
     * @return
     */
    public static String getCap(String str, boolean flag){
        StringBuilder sb = new StringBuilder();
        int length = str != null ? str.length() : 0;
        if(length >= 1){
            if(flag){
                sb.append(str.substring(0, 1).toUpperCase());
            }else{
                sb.append(str.substring(0, 1).toLowerCase());
            }
            if(length > 1){
                sb.append(str.substring(1));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int count = count("select * from album where id = ?", ps -> {
            try {
                ps.setLong(1, 1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        System.out.println(count);
    }
}
