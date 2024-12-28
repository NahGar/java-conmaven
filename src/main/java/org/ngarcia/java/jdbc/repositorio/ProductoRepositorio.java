package org.ngarcia.java.jdbc.repositorio;

import org.ngarcia.java.jdbc.modelo.Producto;
import org.ngarcia.java.jdbc.util.ConexionBaseDatos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorio implements Repositorio<Producto> {

    @Override
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();

        //autoclose de stmt y rs
        try(Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM productos")) {

            while (rs.next()) {
                Producto producto = crearProducto(rs);
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    @Override
    public Producto porId(Long id) {

        Producto producto = null;

        //autoclose de stmt y rs
        try (PreparedStatement stmt = getConnection()
                .prepareStatement("SELECT * FROM productos WHERE Id=?")) {

            stmt.setLong(1,id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                producto = crearProducto(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }

    @Override
    public void guardar(Producto producto) {

    }

    @Override
    public void eliminar(Long id) {

    }


    private Connection getConnection() throws SQLException {
        return ConexionBaseDatos.getInstance();
    }

    private static Producto crearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId( rs.getLong("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setPrecio(rs.getInt("precio"));
        producto.setFecha_registro(rs.getDate("fecha_registro"));
        return producto;
    }
}
