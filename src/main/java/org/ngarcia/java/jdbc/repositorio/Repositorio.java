package org.ngarcia.java.jdbc.repositorio;

import java.sql.SQLException;
import java.util.List;

public interface Repositorio<T> {

    List<T> listar();
    T porId(Long id);
    Long guardar(T t);
    void eliminar(Long id);
}
