package org.ngarcia.java.jdbc;

import java.sql.*;

public class EjecutarJDBC {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3307/java_curso?serverTimezone=America/Montevideo";
        String user = "root";
        String pass = "root";

        /*
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        try {
            conn = DriverManager.getConnection(url,user,pass);
            stmt = conn.createStatement();
            result = stmt.executeQuery("SELECT * FROM productos");
            while (result.next()) {
                System.out.println(result.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
         */

        //Simplificado con autoclose
        try
        (Connection conn = DriverManager.getConnection(url,user,pass);
         Statement stmt = conn.createStatement();
         ResultSet result = stmt.executeQuery("SELECT * FROM productos")) {

            while (result.next()) {
                System.out.print(result.getInt("id")+" ");
                System.out.print(result.getString("nombre") + " ");
                System.out.print(result.getInt("precio")+ " ");
                System.out.println(result.getDate("fecha_registro"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
