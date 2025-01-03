package Tareas.MantenedorUsuariosJdbc;

import Tareas.MantenedorUsuariosJdbc.models.Usuario;
import Tareas.MantenedorUsuariosJdbc.repositorio.UsuarioRepositorio;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Ejecutar {
    public static void main(String[] args) {

        UsuarioRepositorio repo = new UsuarioRepositorio();

        Map<String, Integer> operaciones = new HashMap();
        operaciones.put("Actualizar", 1);
        operaciones.put("Eliminar", 2);
        operaciones.put("Agregar", 3);
        operaciones.put("Listar", 4);
        operaciones.put("Salir", 5);

        Object[] opArreglo = operaciones.keySet().toArray();

        int opcionIndice = 0;
        while(opcionIndice != 5) {

            Object opcion = JOptionPane.showInputDialog(null,
                    "Seleccione una operación",
                    "Mantenedor de usuarios",
                    JOptionPane.INFORMATION_MESSAGE, null, opArreglo, opArreglo[0]);
            if (opcion == null) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una operación");
            } else {
                opcionIndice = operaciones.get(opcion.toString());
                Usuario usuario;

                switch (opcionIndice) {
                    case 1:
                        usuario = getValoresJOptionPane("UPD");
                        repo.actualizar(usuario);
                        break;
                    case 2:
                        usuario = getValoresJOptionPane("DLT");
                        repo.eliminar(usuario.getId());
                        break;
                    case 3:
                        usuario = getValoresJOptionPane("INS");
                        Long idCrear = repo.crear(usuario);
                        break;
                    case 4:
                        repo.listar().forEach(System.out::println);
                        break;
                    case 5:
                        repo.salir();
                        break;
                }

            }
        }

    }

    private static Usuario getValoresJOptionPane(String mode) {

        Usuario usuario = new Usuario();
        Long Id = null;

        if (mode.equals("DLT") || mode.equals("UPD")) {
            String idString = JOptionPane.showInputDialog(null, "Ingrese id:", "Entrada de datos", JOptionPane.QUESTION_MESSAGE);
            Id = Long.parseLong(idString);
            usuario.setId(Id);
        }

        if (mode.equals("INS") || mode.equals("UPD")) {

            String username = JOptionPane.showInputDialog(null, "Ingrese username:", "Entrada de datos", JOptionPane.QUESTION_MESSAGE);
            String password = JOptionPane.showInputDialog(null, "Ingrese password:", "Entrada de datos", JOptionPane.QUESTION_MESSAGE);
            String email = JOptionPane.showInputDialog(null, "Ingrese email:", "Entrada de datos", JOptionPane.QUESTION_MESSAGE);
            usuario.setUsername(username);
            usuario.setPassword(password);
            usuario.setEmail(email);
        }

        return usuario;
    }
}
