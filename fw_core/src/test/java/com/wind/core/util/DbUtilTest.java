package com.wind.core.util;

import com.alibaba.fastjson.JSON;
import com.wind.core.model.JdbcProp;
import com.wind.core.model.db.Table;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @package com.wind.core.util
 * @className DbUtilTest
 * @note db测试
 * @author wind
 * @date 2020/12/9 23:32
 */
public class DbUtilTest {

    @Test
    public void testTable(){
        JdbcProp jdbcProp = new JdbcProp();
        jdbcProp.setDriver("com.mysql.jdbc.Driver");
        jdbcProp.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull");
        jdbcProp.setUser("root");
        jdbcProp.setPassword("123456");
        DbUtil dbUtil = new DbUtil(jdbcProp);
        Table table = dbUtil.getTable("test", "user");
        System.out.println(JSON.toJSONString(table));
    }

    @Test
    public void testMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", "wind");
        System.out.println(JSON.toJSONString(map));
    }
}
