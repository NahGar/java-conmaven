package org.ngarcia.java.jdbc.repositorio;

import java.sql.SQLException;
import java.util.List;

public interface RepositorioTrx<T> {

    List<T> listar() throws SQLException;
    T porId(Long id) throws SQLException;
    Long guardar(T t) throws SQLException;
    void eliminar(Long id) throws SQLException;
}
