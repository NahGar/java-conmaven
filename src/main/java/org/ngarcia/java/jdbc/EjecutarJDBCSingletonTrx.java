package org.ngarcia.java.jdbc;

import org.ngarcia.java.jdbc.modelo.*;
import org.ngarcia.java.jdbc.repositorio.*;
import org.ngarcia.java.jdbc.util.ConexionBaseDatosSingletonTrx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class EjecutarJDBCSingletonTrx {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = ConexionBaseDatosSingletonTrx.getInstance()) {

            //para que no haga commit automático. se hace al cerrar conexión si esta auto
            if(conn.getAutoCommit()) { conn.setAutoCommit(false); }

            try { //segundo try con conn para poder hacer commit o rollback
                RepositorioTrx<Producto> repo = new ProductoRepositorioTrx();
                System.out.println("---------- listar ----------");
                repo.listar().forEach(System.out::println);
                System.out.println("---------- obtener por id ----------");
                System.out.println(repo.porId(1L));

                System.out.println("---------- insert ----------");
                Producto producto = new Producto();
                producto.setNombre("Mousepad");
                producto.setPrecio(50);
                producto.setFecha_registro(new Date());
                Categoria categoria = new Categoria();
                categoria.setId(3L);
                producto.setCategoria(categoria);
                Long idInsert = repo.guardar(producto);

                System.out.println("---------- update " + idInsert + " ----------");
                producto.setId(idInsert);
                producto.setNombre("Mouse");
                producto.setPrecio(3000);
                producto.setSku("abc"); //ya existe (error hace rollback)
                //producto.setSku("abc123"); //no existe
                repo.guardar(producto);

                repo.listar().forEach(System.out::println);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }

        }

    }
}
