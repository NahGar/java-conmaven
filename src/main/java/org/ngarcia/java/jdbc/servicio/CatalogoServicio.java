package org.ngarcia.java.jdbc.servicio;

import org.ngarcia.java.jdbc.modelo.*;
import org.ngarcia.java.jdbc.repositorio.*;
import org.ngarcia.java.jdbc.util.ConexionBaseDatosPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CatalogoServicio implements Servicio {

    private RepositorioTrxServicio<Producto> productoRepositorio;
    private RepositorioTrxServicio<Categoria> categoriaRepositorio;

    public CatalogoServicio() {
        this.productoRepositorio = new ProductoRepositorioTrxServicio();
        this.categoriaRepositorio = new CategoriaRepositorioTrxServicio();
    }

    @Override
    public List<Producto> listarProductos() throws SQLException {
        try(Connection conn = ConexionBaseDatosPool.getConnection()) {
            productoRepositorio.setConn(conn);
            return productoRepositorio.listar();
        }
    }

    @Override
    public Producto productoPorId(Long id) throws SQLException {
        try(Connection conn = ConexionBaseDatosPool.getConnection()) {
            productoRepositorio.setConn(conn);
            return productoRepositorio.porId(id);
        }
    }

    @Override
    public Long guardarProducto(Producto producto) throws SQLException {
        try(Connection conn = ConexionBaseDatosPool.getConnection()) {

            Long id = null;
            productoRepositorio.setConn(conn);
            if(conn.getAutoCommit()) { conn.setAutoCommit(false); }
            try {
                id = productoRepositorio.guardar(producto);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
            return id;
        }
    }

    @Override
    public void eliminarProducto(Long id) throws SQLException {
        try(Connection conn = ConexionBaseDatosPool.getConnection()) {
            productoRepositorio.setConn(conn);
            if(conn.getAutoCommit()) { conn.setAutoCommit(false); }
            try {
                productoRepositorio.eliminar(id);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Categoria> listarCategorias() throws SQLException {
        try(Connection conn = ConexionBaseDatosPool.getConnection()) {
            categoriaRepositorio.setConn(conn);
            return categoriaRepositorio.listar();
        }
    }

    @Override
    public Categoria categoriaPorId(Long id) throws SQLException {
        try(Connection conn = ConexionBaseDatosPool.getConnection()) {
            categoriaRepositorio.setConn(conn);
            return categoriaRepositorio.porId(id);
        }
    }

    @Override
    public Long guardarCategoria(Categoria categoria) throws SQLException {
        try(Connection conn = ConexionBaseDatosPool.getConnection()) {
            Long id = null;
            categoriaRepositorio.setConn(conn);
            if(conn.getAutoCommit()) { conn.setAutoCommit(false); }
            try {
                id = categoriaRepositorio.guardar(categoria);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
            return id;
        }
    }

    @Override
    public void eliminarCategoria(Long id) throws SQLException {
        try(Connection conn = ConexionBaseDatosPool.getConnection()) {
            categoriaRepositorio.setConn(conn);
            if(conn.getAutoCommit()) { conn.setAutoCommit(false); }
            try {
                categoriaRepositorio.eliminar(id);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void guardarProductoConCategoria(Producto producto, Categoria categoria) throws SQLException {
        try(Connection conn = ConexionBaseDatosPool.getConnection()) {
            productoRepositorio.setConn(conn);
            categoriaRepositorio.setConn(conn);

            if(conn.getAutoCommit()) { conn.setAutoCommit(false); }
            try {
                Long catId = categoriaRepositorio.guardar(categoria);
                categoria.setId(catId);
                producto.setCategoria(categoria);
                Long prodId = productoRepositorio.guardar(producto);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        }
    }
}
