package org.ngarcia.java.jdbc;

import org.ngarcia.java.jdbc.modelo.*;
import org.ngarcia.java.jdbc.repositorio.*;

import java.util.Date;

public class EjecutarJDBCCRUDClose {
    public static void main(String[] args) {

        Repositorio<Producto> repo = new ProductoRepositorioClose();
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
        repo.listar().forEach(System.out::println);

        System.out.println("---------- update ----------");
        producto.setId(idInsert);
        producto.setNombre("Mouse");
        producto.setPrecio(3000);
        repo.guardar(producto);
        repo.listar().forEach(System.out::println);

        System.out.println("---------- delete ----------");
        repo.eliminar(idInsert);
        repo.listar().forEach(System.out::println);

    }
}
