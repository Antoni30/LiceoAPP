package com.liceo.notas.repositories;

import com.liceo.notas.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad {@link Usuario}.
 * Proporciona operaciones CRUD básicas y métodos personalizados para acceder a los usuarios del sistema.
 * Utiliza Spring Data JPA para interactuar con la base de datos.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, String> { // ID es String (idUsuario)

    /**
     * Busca un usuario por su nickname único.
     * Retorna un {@link Optional} para manejar de forma segura el caso en que no se encuentre el usuario.
     *
     * @param nickname Nickname del usuario a buscar
     * @return Optional conteniendo el usuario si se encuentra, o vacío si no existe
     */
    Optional<Usuario> findByNickname(String nickname);
    Optional<Usuario> findByIdUsuario(String idUsuario);
    /**
     * Busca todos los usuarios que tengan el estado especificado.
     *
     * @param estado Estado del usuario a filtrar (ej: "Activo", "Inactivo")
     * @return Lista de usuarios con el estado dado
     */
    List<Usuario> findByEstado(String estado);

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * Útil para verificar unicidad o recuperación de cuenta.
     *
     * @param email Correo electrónico del usuario
     * @return Optional conteniendo el usuario si se encuentra, o vacío si no existe
     */


    Optional<Usuario> findByNicknameAndEmail(String nickname, String email);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTokenVerificacion(String token);
    // Opcional: Si necesitas validar el email también
    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario AND u.email = :email")
    Optional<Usuario> findByIdUsuarioAndEmail(
            @Param("idUsuario") String idUsuario,
            @Param("email") String email
    );
    /**
     * Busca un usuario por su nickname y contraseña.
     * Utilizado durante el proceso de autenticación.
     *
     * @param nickname   Nickname del usuario
     * @param contrasena Contraseña proporcionada por el usuario
     * @return Optional conteniendo el usuario si las credenciales son válidas, o vacío si no coincide
     */
    @Query("SELECT u FROM Usuario u WHERE u.nickname = :nickname AND u.contrasena = :contrasena")
    Optional<Usuario> findByNicknameAndContrasena(
            @Param("nickname") String nickname,
            @Param("contrasena") String contrasena);
}