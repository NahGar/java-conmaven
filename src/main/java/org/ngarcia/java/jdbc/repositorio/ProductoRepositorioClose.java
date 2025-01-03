package org.ngarcia.java.jdbc.repositorio;

import org.ngarcia.java.jdbc.modelo.*;
import org.ngarcia.java.jdbc.util.ConexionBaseDatosNoSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioClose implements Repositorio<Producto> {

    @Override
    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT p.*, c.nombre as categoria FROM productos as p " +
                "INNER JOIN categorias as c ON (p.categoria_id = c.id)";

        //autoclose de conn, stmt y rs
        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

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

        String sql = "SELECT p.*, c.nombre as categoria FROM productos as p " +
                     "INNER JOIN categorias as c ON (p.categoria_id = c.id) WHERE p.Id=?";

        //autoclose de conn, stmt y rs
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
    public Long guardar(Producto producto) {
        
        String sql;
        Long generatedId = null;
        boolean isUpdate = producto.getId() != null && producto.getId() > 0;
        
        if(isUpdate) {
            sql = "UPDATE productos set nombre=?, precio=?, categoria_id=? " +
                  "WHERE id=?";
        }
        else {
            sql = "INSERT INTO productos(nombre,precio,categoria_id" +
                    ",fecha_registro) VALUES (?,?,?,?)";
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1,producto.getNombre());
            stmt.setInt(2,producto.getPrecio());
            stmt.setLong(3,producto.getCategoria().getId());

            if(isUpdate) {
                stmt.setLong(4,producto.getId());
            }
            else {
                //convierte a java.sql
                stmt.setDate(4, new Date(producto.getFecha_registro().getTime()));
            }

            int affectedRows = stmt.executeUpdate();
            
            // Obtener el ID generado si es un INSERT
            if (!isUpdate && affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getLong(1);
                    }
                }
            }   
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return generatedId;
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM productos WHERE id=?")) {

            stmt.setLong(1,id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Connection getConnection() throws SQLException {
        return ConexionBaseDatosNoSingleton.getInstance();
    }

    private static Producto crearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId( rs.getLong("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setPrecio(rs.getInt("precio"));
        producto.setFecha_registro(rs.getDate("fecha_registro"));
        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("categoria_id"));
        categoria.setNombre(rs.getString("categoria"));
        producto.setCategoria(categoria);
        return producto;
    }
}
