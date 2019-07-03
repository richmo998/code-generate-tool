package com.xhg.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceCommponent {
    private static Map<String, DataSource> dsMap = new HashMap<>();


    @Value("${config.db.url}")
    private String dbUrl;
    @Value("${config.db.username}")
    private String username;
    @Value("${config.db.password}")
    private String password;

    public DataSource getDs(String dbName) {
        if (StringUtils.isBlank(dbName)) {
            return null;
        }
        DataSource ds = dsMap.get(dbName);
        if (ds != null) {
            return ds;
        }

        ds = dataSource(dbName);
        dsMap.put(dbName, ds);
        return ds;
    }


    private DataSource dataSource(String dbName) {
        DruidDataSource datasource = new DruidDataSource();
        try {
            datasource.setUrl(dbUrl + dbName + "?characterEncoding=utf8&serverTimezone=Asia/Shanghai");
            datasource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            datasource.setUsername(username);
            datasource.setPassword(password);
            datasource.setInitialSize(1);
            datasource.setMinIdle(2);
            datasource.setMaxWait(60000);
            datasource.setMaxActive(5);
            datasource.setMinEvictableIdleTimeMillis(300000);
            datasource.setTimeBetweenEvictionRunsMillis(60000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datasource;
    }
}
