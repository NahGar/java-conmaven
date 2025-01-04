package org.ngarcia.java.jdbc;

import org.ngarcia.java.jdbc.util.ConexionBaseDatosSingleton;

import java.sql.*;

public class EjecutarJDBCSingleton {
    public static void main(String[] args) {

        //Simplificado con autoclose
        try
        (Connection conn = ConexionBaseDatosSingleton.getInstance();
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
