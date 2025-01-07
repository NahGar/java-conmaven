package org.ngarcia.java.jdbc;

import org.ngarcia.java.jdbc.modelo.*;
import org.ngarcia.java.jdbc.repositorio.*;
import org.ngarcia.java.jdbc.util.ConexionBaseDatosPool;
import org.ngarcia.java.jdbc.util.ResetAutoIncrement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import org.ngarcia.java.jdbc.servicio.CatalogoServicio;
import org.ngarcia.java.jdbc.servicio.Servicio;

public class EjecutarJDBCServicio {
    public static void main(String[] args) throws SQLException {

        Servicio servicio = new CatalogoServicio();
        
        System.out.println("---------- obtener por id categorias ----------");
        Categoria categoria = new Categoria();
        categoria = servicio.categoriaPorId(1L);
        System.out.println(categoria);
        
        System.out.println("---------- obtener por id ----------");
        System.out.println(servicio.productoPorId(1L));

        System.out.println("---------- insertar categoria ----------");
        categoria = new Categoria();
        categoria.setNombre("Ropa");

        System.out.println("---------- insert producto ----------");
        Producto producto = new Producto();
        producto.setNombre("Polera");
        producto.setPrecio(50);
        producto.setFecha_registro(new Date());
        producto.setSku("abc123"); 
        producto.setCategoria(categoria);

        servicio.guardarProductoConCategoria(producto, categoria);

        System.out.println("---------- listar categorias ----------");
        servicio.listarCategorias().forEach(System.out::println);
        System.out.println("---------- listar productos ----------");
        servicio.listarProductos().forEach(System.out::println);
            
    }
}
