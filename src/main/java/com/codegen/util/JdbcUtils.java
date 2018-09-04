package com.codegen.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

@SuppressWarnings("static-access")
public class JdbcUtils {
	
	private static DataSource ds = null;

    static {
        try {
            Properties prop = new Properties();
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
            prop.load(in);
            BasicDataSourceFactory factory = new BasicDataSourceFactory();
            ds = factory.createDataSource(prop);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static DataSource getDataSource() {
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    public static void main(String[] args) throws SQLException {
		System.out.println(getConnection());
	}
   
}
