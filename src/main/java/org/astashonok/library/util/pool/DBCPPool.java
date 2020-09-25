package org.astashonok.library.util.pool;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBCPPool implements ConnectionPool {

    private BasicDataSource dataSource;

    private DBCPPool(){
        this.dataSource = new BasicDataSource();
        ResourceBundle resource = ResourceBundle.getBundle("db");
        dataSource.setUrl(resource.getString("url"));
        dataSource.setUsername(resource.getString("userName"));
        dataSource.setPassword(resource.getString("password"));
        dataSource.setMinIdle(Integer.parseInt(resource.getString("minIdle")));
        dataSource.setMaxIdle(Integer.parseInt(resource.getString("maxIdle")));
        dataSource.setMaxOpenPreparedStatements(Integer.parseInt(resource.getString("maxOpenPreparedStatements")));
    }

    private static class ConnectionPoolHandler{
        private final static ConnectionPool instance = new DBCPPool();
    }

    public static ConnectionPool getInstance(){
        return ConnectionPoolHandler.instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}