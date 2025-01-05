package org.ngarcia.java.jdbc.repositorio;

import org.ngarcia.java.jdbc.modelo.Categoria;
import org.ngarcia.java.jdbc.util.ResetAutoIncrement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositorio implements RepositorioTrx<Categoria>{

    private Connection conn;

    public CategoriaRepositorio(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Categoria> listar() throws SQLException {

        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT * FROM categorias";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.add(crearCategoria(rs));
            }
        }
        return categorias;
    }

    @Override
    public Categoria porId(Long id) throws SQLException {

        Categoria categoria = null;
        String sql = "SELECT * FROM categorias WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1,id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    categoria = crearCategoria(rs);
                }
            }
        }
        return categoria;
    }

    @Override
    public Long guardar(Categoria categoria) throws SQLException {

        String sql;
        Long generatedId = null;
        boolean isUpdate = categoria.getId() != null && categoria.getId() > 0;

        if(isUpdate) {
            sql = "UPDATE categorias set nombre=? WHERE id=?";
        }
        else {
            sql = "INSERT INTO categorias(nombre) VALUES (?)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNombre());
            if (isUpdate) {
                stmt.setLong(2, categoria.getId());
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

        String sql = "DELETE FROM categorias WHERE id=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private static Categoria crearCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("id"));
        categoria.setNombre(rs.getString("nombre"));
        return categoria;
    }
}
