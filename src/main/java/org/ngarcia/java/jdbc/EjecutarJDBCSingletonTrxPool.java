package org.ngarcia.java.jdbc;

import org.ngarcia.java.jdbc.modelo.Categoria;
import org.ngarcia.java.jdbc.modelo.Producto;
import org.ngarcia.java.jdbc.repositorio.CategoriaRepositorio;
import org.ngarcia.java.jdbc.repositorio.ProductoRepositorioTrx;
import org.ngarcia.java.jdbc.repositorio.ProductoRepositorioTrxPool;
import org.ngarcia.java.jdbc.repositorio.RepositorioTrx;
import org.ngarcia.java.jdbc.util.ConexionBaseDatosPool;
import org.ngarcia.java.jdbc.util.ResetAutoIncrement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class EjecutarJDBCSingletonTrxPool {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = ConexionBaseDatosPool.getConnection()) {

            //para que no haga commit automático. se hace al cerrar conexión si esta auto
            if(conn.getAutoCommit()) { conn.setAutoCommit(false); }

            try { //segundo try con conn para poder hacer commit o rollback
                RepositorioTrx<Categoria> repoCat = new CategoriaRepositorio(conn);
                System.out.println("---------- listar categorias ----------");
                repoCat.listar().forEach(System.out::println);

                System.out.println("---------- obtener por id categorias ----------");
                Categoria categoria = new Categoria();
                categoria = repoCat.porId(1L);
                System.out.println(categoria);

                System.out.println("---------- insertar categoria ----------");
                categoria = new Categoria();
                categoria.setNombre("Ropa");
                Long idCategoria = repoCat.guardar(categoria);

                System.out.println("---------- update categoria " + idCategoria + " ----------");
                categoria = repoCat.porId(idCategoria);
                categoria.setNombre("Ropa verano");
                repoCat.guardar(categoria);

                RepositorioTrx<Producto> repoProd = new ProductoRepositorioTrxPool(conn);
                System.out.println("---------- listar productos ----------");
                repoProd.listar().forEach(System.out::println);
                System.out.println("---------- obtener por id ----------");
                System.out.println(repoProd.porId(1L));

                System.out.println("---------- insert ----------");
                Producto producto = new Producto();
                producto.setNombre("Polera");
                producto.setPrecio(50);
                producto.setFecha_registro(new Date());
                //categoria = new Categoria();
                //categoria.setId(idCategoria);
                producto.setCategoria(categoria);
                Long idInsert = repoProd.guardar(producto);

                System.out.println("---------- update " + idInsert + " ----------");
                producto.setId(idInsert);
                producto.setNombre("Polera roja");
                producto.setPrecio(3000);
                //producto.setSku("abc"); //ya existe (error hace rollback)
                producto.setSku("abc1234"); //no existe
                repoProd.guardar(producto);

                repoProd.listar().forEach(System.out::println);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }

        }

    }
}
