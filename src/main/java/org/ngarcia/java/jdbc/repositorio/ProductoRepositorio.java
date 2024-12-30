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
            //autoclose rs
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    producto = crearProducto(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }

    @Override
    public void guardar(Producto producto) {
        String sql;
        if(producto.getId() != null && producto.getId() > 0) {
            sql = "UPDATE productos set nombre=?, precio=? " +
                  "WHERE id=?";
        }
        else {
            sql = "INSERT INTO productos(nombre,precio,fecha_registro) " +
                    "VALUES (?,?,?)";
        }

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1,producto.getNombre());
            stmt.setInt(2,producto.getPrecio());

            if(producto.getId() != null && producto.getId() > 0) {
                stmt.setLong(3,producto.getId());
            }
            else {
                //convierte a java.sql
                stmt.setDate(3, new Date(producto.getFecha_registro().getTime()));
            }
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Long id) {
        try (PreparedStatement stmt = getConnection()
                .prepareStatement("DELETE FROM productos WHERE id=?")) {

            stmt.setLong(1,id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
