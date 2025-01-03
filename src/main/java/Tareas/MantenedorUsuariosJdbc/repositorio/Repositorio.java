package Tareas.MantenedorUsuariosJdbc.repositorio;

import java.util.List;

public interface Repositorio<T> {

    void actualizar(T t);
    void eliminar(Long id);
    Long crear(T t);
    List<T> listar();
    void salir();

}
