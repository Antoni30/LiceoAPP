package com.liceo.notas.services;

import com.liceo.notas.dtos.UsuarioDTO;
import com.liceo.notas.entities.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de usuarios del sistema.
 * Define las operaciones básicas para crear, consultar, actualizar y eliminar usuarios,
 * así como para buscar usuarios por su estado.
 */
public interface UsuarioService {
    /**
     * Crea un nuevo usuario con los datos proporcionados en el DTO.
     *
     * @param dto Objeto {@link UsuarioDTO} con los datos del usuario a crear
     * @return {@link UsuarioDTO} con los datos registrados, incluyendo su ID generado si aplica
     */
    UsuarioDTO crearUsuario(UsuarioDTO dto);

    /**
     * Obtiene los datos de un usuario por su identificador único (ID personalizado).
     *
     * @param id ID del usuario a obtener (generalmente una cadena alfanumérica)
     * @return Optional conteniendo el {@link UsuarioDTO} si se encuentra, o vacío si no existe
     */
    Optional<UsuarioDTO> obtenerPorId(String id);

    /**
     * Obtiene una lista con todos los usuarios registrados en el sistema.
     *
     * @return Lista de objetos {@link UsuarioDTO} con todos los usuarios disponibles
     */
    List<UsuarioDTO> listarTodos();

    /**
     * Actualiza los datos de un usuario existente identificado por su ID.
     *
     * @param id  ID del usuario a actualizar
     * @param dto Objeto {@link UsuarioDTO} con los nuevos datos del usuario
     * @return {@link UsuarioDTO} con los datos actualizados
     */
    Optional<UsuarioDTO> actualizarUsuario(String id, UsuarioDTO dto);

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param id ID del usuario a eliminar
     * @return true si el usuario fue eliminado correctamente, false si no se encontró
     */
    boolean eliminarUsuario(String id);

    /**
     * Obtiene una lista de usuarios cuyo estado coincide con el especificado.
     * El estado puede ser "Activo", "Inactivo", etc.
     *
     * @param estado Estado del usuario para filtrar los resultados
     * @return Lista de objetos {@link UsuarioDTO} con usuarios en el estado indicado
     */
    List<UsuarioDTO> listarPorEstado(String estado);

    void  enviarEmailVerificacion(Usuario usuario);
    // Nuevos métodos agregados
    boolean verificarEmail(String token);
}