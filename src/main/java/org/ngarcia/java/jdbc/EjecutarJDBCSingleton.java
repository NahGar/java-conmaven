package org.ngarcia.java.jdbc;

import org.ngarcia.java.jdbc.modelo.Producto;
import org.ngarcia.java.jdbc.repositorio.*;
import org.ngarcia.java.jdbc.util.ConexionBaseDatos;

import java.sql.*;
import java.util.List;

public class EjecutarJDBCSingleton {
    public static void main(String[] args) {

        //Simplificado con autoclose
        /*
        try
        (Connection conn = ConexionBaseDatos.getInstance();
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
        */

        // el connection en el try solo se usa para autoclose
        try (Connection conn = ConexionBaseDatos.getInstance()) {

            Repositorio<Producto> repo = new ProductoRepositorio();
            repo.listar().forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
