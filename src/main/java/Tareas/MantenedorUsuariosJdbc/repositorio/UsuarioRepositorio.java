package Tareas.MantenedorUsuariosJdbc.repositorio;

import Tareas.MantenedorUsuariosJdbc.models.Usuario;
import org.ngarcia.java.jdbc.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorio implements Repositorio<Usuario>{
    @Override
    public void actualizar(Usuario usuario) {

        String sql = "UPDATE usuarios set username=?, password=?, email=? " +
                     "WHERE id=?";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1,usuario.getUsername());
            stmt.setString(2,usuario.getPassword());
            stmt.setString(3,usuario.getEmail());
            stmt.setLong(4,usuario.getId());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Long id) {

        try (PreparedStatement stmt = getConn()
                .prepareStatement("DELETE FROM usuarios WHERE id=?")) {

            stmt.setLong(1,id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Long crear(Usuario usuario) {
        Long generatedId = null;

        String sql = "INSERT INTO usuarios (username,password,email) " +
                "VALUES (?,?,?)";

        try (PreparedStatement stmt = getConn()
                .prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1,usuario.getUsername());
            stmt.setString(2,usuario.getPassword());
            stmt.setString(3,usuario.getEmail());

            int affectedRows = stmt.executeUpdate();

            // Obtener el ID generado si es un INSERT
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getLong(1);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    @Override
    public List<Usuario> listar() {

        List<Usuario> usuarios = new ArrayList<>();
        try (Statement stmt = getConn().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios")) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEmail(rs.getString("email"));
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public void salir() {
        Connection conn = null;
        try {
            conn = getConn();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConn() throws SQLException {
        return ConexionBaseDatosSingleton.getInstance();
    }

}
