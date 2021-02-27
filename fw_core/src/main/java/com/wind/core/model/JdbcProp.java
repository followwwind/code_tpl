package com.wind.core.model;

import java.io.Serializable;

/**
 * @package com.wind.core.model
 * @className JdbcProp
 * @note jdbc配置信息
 * @author wind
 * @date 2020/12/9 20:53
 */
public class JdbcProp implements Serializable {

    private String jdbcUrl;

    private String driver;

    private String user;

    private String password;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
