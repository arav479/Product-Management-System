package com.pms;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static oracle.jdbc.driver.resource.ResourceType.USERNAME;

public class DatabaseConnector {

        static String dbUrl = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
        static String username  = "SMARTSTOCK";
        static String password = "SmartStock123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl,username,password);
    }
    }
