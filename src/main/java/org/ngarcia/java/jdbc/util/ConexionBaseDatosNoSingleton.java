package org.ngarcia.java.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatosNoSingleton {

    private static String url = "jdbc:mysql://localhost:3307/java_curso?serverTimezone=America/Montevideo";
    private static String user = "root";
    private static String pass = "root";
    private static Connection connection;

    public static Connection getInstance() throws SQLException {
        return DriverManager.getConnection(url,user,pass);
    }
}