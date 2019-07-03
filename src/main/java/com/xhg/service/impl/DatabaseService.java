package com.xhg.service.impl;

import com.xhg.service.IDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DatabaseService implements IDatabaseService {

    @Autowired
    private DataSource dataSource;

    @Override
    public List<String> getDbs() {
        List<String> dbList = new ArrayList<>();
        try {
            StringBuffer sql = new StringBuffer();

            sql.append("select table_schema from information_schema.tables ");
            sql.append("where table_schema != 'information_schema' and table_schema != 'mysql'");
            sql.append("and table_schema != 'performance_schema' and table_schema != 'sys'");
            sql.append("group by table_schema");

            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String dbName = rs.getString(1);
                dbList.add(dbName);
            }
            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            log.error("getDbs", e);
        }
        return dbList;
    }
}
