package org.ngarcia.java.jdbc.repositorio;

import org.ngarcia.java.jdbc.modelo.*;
import org.ngarcia.java.jdbc.util.ConexionBaseDatosSingletonTrx;
import org.ngarcia.java.jdbc.util.ResetAutoIncrement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioTrxServicio implements RepositorioTrxServicio<Producto> {

    private Connection conn;

    public ProductoRepositorioTrxServicio() {}

    public ProductoRepositorioTrxServicio(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT p.*, c.nombre as categoria FROM productos as p " +
                "INNER JOIN categorias as c ON (p.categoria_id = c.id)";

        //autoclose de stmt y rs
        try(Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto producto = crearProducto(rs);
                productos.add(producto);
            }
        }
        return productos;
    }

    @Override
    public Producto porId(Long id) throws SQLException {

        Producto producto = null;

        String sql = "SELECT p.*, c.nombre as categoria FROM productos as p " +
                     "INNER JOIN categorias as c ON (p.categoria_id = c.id) WHERE p.Id=?";

        //autoclose de stmt y rs
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1,id);
            //autoclose rs
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    producto = crearProducto(rs);
                }
            }
        }
        return producto;
    }

    @Override
    public Long guardar(Producto producto) throws SQLException {
        
        String sql;
        Long generatedId = null;
        boolean isUpdate = producto.getId() != null && producto.getId() > 0;
        
        if(isUpdate) {
            sql = "UPDATE productos set nombre=?, precio=?, categoria_id=?, sku=? " +
                  "WHERE id=?";
        }
        else {
            ResetAutoIncrement.exec("productos");

            sql = "INSERT INTO productos(nombre,precio,categoria_id,sku" +
                    ",fecha_registro) VALUES (?,?,?,?,?)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getPrecio());
            stmt.setLong(3, producto.getCategoria().getId());
            stmt.setString(4, producto.getSku());

            if (isUpdate) {
                stmt.setLong(5, producto.getId());
            } else {
                //convierte a java.sql
                stmt.setDate(5, new Date(producto.getFecha_registro().getTime()));
            }

            int affectedRows = stmt.executeUpdate();

            //Otra forma de obtener el ultimo id es con SELECT LAST_INSERT_ID()

            // Obtener el ID generado si es un INSERT
            if (!isUpdate && affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getLong(1);
                    }
                }
            }   
        }
        
        return generatedId;
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM productos WHERE id=?")) {

            stmt.setLong(1,id);
            stmt.executeUpdate();
        }
    }

    private Connection getConnection() throws SQLException {
        return ConexionBaseDatosSingletonTrx.getInstance();
    }

    public void setConn(Connection conn) {
        this.conn = conn;
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
        producto.setSku(rs.getString("sku"));
        return producto;
    }
}
