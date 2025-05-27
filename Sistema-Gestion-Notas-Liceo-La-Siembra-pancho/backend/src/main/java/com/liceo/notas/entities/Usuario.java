package com.liceo.notas.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa un usuario del sistema (estudiante, docente o administrador).
 * Mapea la tabla "USUARIO" en la base de datos.
 * Un usuario tiene información personal, credenciales de acceso, estado,
 * y puede estar asociado a uno o más roles y cursos.
 */
@Data
@Entity
@Table(name = "USUARIO")
public class Usuario {

    /**
     * Identificador único del usuario.
     * No es autoincremental, sino un ID personalizado (ej: matrícula o identificación única).
     * - nullable: false → todo usuario debe tener un ID asignado.
     * - length: 10 → longitud máxima permitida para el ID del usuario.
     */
    @Id
    @Column(name = "ID_USUARIO", length = 10, nullable = false)
    private String idUsuario;

    /**
     * Nombres del usuario.
     * - nullable: false → todo usuario debe tener nombres registrados.
     * - length: 50 → longitud máxima permitida para el campo.
     */
    @Column(name = "NOMBRES_USUARIO", length = 50, nullable = false)
    private String nombres;

    /**
     * Apellidos del usuario.
     * - nullable: false → todo usuario debe tener apellidos registrados.
     * - length: 50 → longitud máxima permitida para el campo.
     */
    @Column(name = "APELLIDOS_USUARIO", length = 50, nullable = false)
    private String apellidos;

    /**
     * Nombre de usuario único utilizado para iniciar sesión.
     * - nullable: false → todo usuario debe tener un nickname.
     * - length: 30 → longitud máxima permitida para el campo.
     * - unique: true → no pueden existir dos usuarios con el mismo nickname.
     */
    @Column(name = "NICKNAME_USUARIO", length = 30, nullable = false, unique = true)
    private String nickname;

    /**
     * Contraseña del usuario utilizada para autenticación.
     * - nullable: false → todo usuario debe tener una contraseña asignada.
     * - length: 255 → longitud suficiente para almacenar contraseñas encriptadas.
     *
     * Nota: En producción se recomienda usar @Transient si se maneja en otra capa,
     * y almacenar la contraseña en formato encriptado.
     */
    @Column(name = "CONTRASENA_USUARIO", length = 255, nullable = false)
    private String contrasena;

    /**
     * Estado actual del usuario en el sistema.
     * Ejemplos: "Activo", "Inactivo".
     * - nullable: false → todo usuario debe tener un estado definido.
     * - length: 20 → longitud máxima permitida para el valor del estado.
     */
    @Column(name = "ESTADO_USUARIO", length = 20, nullable = false)
    private String estado;

    /**
     * Indica si el usuario tiene habilitada la autenticación de dos factores (MFA).
     * Valor por defecto: false.
     */
    @Column(name = "MFA_HABILITADO", nullable = false)
    private Boolean mfaHabilitado = false;

    /**
     * Cadena secreta utilizada para generar códigos MFA (por ejemplo, con Google Authenticator).
     * Puede ser null si MFA no está habilitado para este usuario.
     */
    @Column(name = "MFA_SECRET")
    private String mfaSecret;

    /**
     * Dirección de correo electrónico del usuario.
     * Campo opcional pero útil para notificaciones, recuperación de contraseña y MFA.
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * Fecha y hora de expiración del código MFA temporal.
     * Se usa cuando se genera un código para verificación de inicio de sesión.
     */
    @Column(name = "MFA_CODE_EXPIRATION")
    private LocalDateTime mfaCodeExpiration;

    @Column(name = "EMAIL_VERIFICADO", nullable = false)
    private boolean emailVerificado = false;

    @Column(name = "TOKEN_VERIFICACION", length = 36)
    private String tokenVerificacion;
    /**
     * Lista de roles asociados a este usuario.
     * Relación ManyToMany mapeada a través de la tabla intermedia "USUARIO_ROL".
     * Permite que un usuario tenga múltiples roles (ej: Docente, Estudiante).
     */
    @ManyToMany
    @JoinTable(
            name = "USUARIO_ROL",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_ROL")
    )


    private List<Rol> roles;

    /**
     * Lista de relaciones entre este usuario y los cursos en los que participa.
     * Relación One-to-Many con la entidad UsuarioCurso (tabla intermedia).
     * - mappedBy: indica que la relación está gestionada desde la entidad UsuarioCurso.
     */
    @OneToMany(mappedBy = "usuario")
    private List<UsuarioCurso> cursos;
}