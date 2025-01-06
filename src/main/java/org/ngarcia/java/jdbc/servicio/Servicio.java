package org.ngarcia.java.jdbc.servicio;

import org.ngarcia.java.jdbc.modelo.*;

import java.sql.SQLException;
import java.util.List;

public interface Servicio {
    List<Producto> listarProductos() throws SQLException;
    Producto productoPorId(Long id) throws SQLException;
    Long guardarProducto(Producto producto) throws SQLException;
    void eliminarProducto(Long id) throws SQLException;

    List<Categoria> listarCategorias() throws SQLException;
    Categoria categoriaPorId(Long id) throws SQLException;
    Long guardarCategoria(Categoria categoria) throws SQLException;
    void eliminarCategoria(Long id) throws SQLException;

    void guardarProductoConCategoria(Producto producto,
        Categoria categoria) throws SQLException;
}
