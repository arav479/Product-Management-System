package com.pms;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static oracle.jdbc.driver.resource.ResourceType.USERNAME;

public class DatabaseConnector {

        String dbUrl = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
        String username  = "SMARTSTOCK";
        String password = "SmartStock123";
        Connection connection;
        {
            try {
                connection = DriverManager.getConnection(dbUrl,username,password);
                System.out.println("Connected");
            } catch (SQLException e) {
                System.out.println("Error");
                throw new RuntimeException(e);
            }
        }
    public static Connection getConnection() throws SQLException {
        return this.connection;
    }
    }


