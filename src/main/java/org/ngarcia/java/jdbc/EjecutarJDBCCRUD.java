package org.ngarcia.java.jdbc;

import org.ngarcia.java.jdbc.modelo.Producto;
import org.ngarcia.java.jdbc.repositorio.ProductoRepositorio;
import org.ngarcia.java.jdbc.repositorio.Repositorio;
import org.ngarcia.java.jdbc.util.ConexionBaseDatos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import org.ngarcia.java.jdbc.modelo.Categoria;

public class EjecutarJDBCCRUD {
    public static void main(String[] args) {


        // el connection en el try solo se usa para autoclose
        try (Connection conn = ConexionBaseDatos.getInstance()) {

            Repositorio<Producto> repo = new ProductoRepositorio();
            System.out.println("---------- listar ----------");
            repo.listar().forEach(System.out::println);
            System.out.println("---------- obtener por id ----------");
            System.out.println(repo.porId(1L));

            System.out.println("---------- insert ----------");
            Producto producto = new Producto();
            producto.setNombre("Teclado mecánico");
            producto.setPrecio(5000);
            producto.setFecha_registro(new Date());
            
            Categoria categoria = new Categoria();
            categoria.setId(4L);
            producto.setCategoria(categoria);
            
            repo.guardar(producto);
            repo.listar().forEach(System.out::println);

            System.out.println("---------- update ----------");
            producto.setId(4L);
            producto.setNombre("Mouse");
            producto.setPrecio(3000);
            repo.guardar(producto);
            repo.listar().forEach(System.out::println);

            System.out.println("---------- delete ----------");
            repo.eliminar(4L);
            repo.listar().forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
