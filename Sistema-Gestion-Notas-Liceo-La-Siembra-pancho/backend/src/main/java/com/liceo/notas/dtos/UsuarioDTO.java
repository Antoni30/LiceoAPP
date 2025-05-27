package com.liceo.notas.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.util.List;

/**
 * DTO (Data Transfer Object) que representa un usuario del sistema.
 * Contiene los datos necesarios para la gestión de cuentas de usuario.
 *
 * <p>Se utiliza para transferir información entre las capas de la aplicación
 * y en las comunicaciones con el cliente.</p>
 *
 * <p>La anotación {@code @Data} de Lombok genera automáticamente:
 * <ul>
 *   <li>Métodos getter y setter para todos los campos</li>
 *   <li>Implementaciones de {@code equals()}, {@code hashCode()}</li>
 *   <li>Método {@code toString()}</li>
 *   <li>Constructor sin argumentos</li>
 * </ul>
 */
@Data
public class UsuarioDTO {
    /**
     * Identificador único del usuario en el sistema.
     * Ejemplo: cédula, documento de identidad o UUID.
     * Formato: String para mayor flexibilidad.
     */

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 10, message = "La cédula debe tener exactamente 10 dígitos")
    @Pattern(regexp = "^[0-9]*$", message = "La cédula solo debe contener números")

    private String idUsuario;

    /**
     * Nombres del usuario.
     * Ejemplo: "María José" o "Carlos Andrés".
     * No debe contener números ni caracteres especiales.
     */
    private String nombres;

    /**
     * Apellidos del usuario.
     * Ejemplo: "García López" o "Pérez Rodríguez".
     * No debe contener números ni caracteres especiales.
     */
    private String apellidos;

    /**
     * Nombre de usuario para autenticación (login).
     * Debe ser único en el sistema.
     * Ejemplo: "mgarci23" o "cperez".
     */
    private String nickname;

    /**
     * Contraseña del usuario.
     * En entornos de producción, siempre debe almacenarse encriptada.
     * Debe cumplir con políticas de seguridad mínimas:
     * - Mínimo 8 caracteres
     * - Letras mayúsculas/minúsculas
     * - Números
     * - Caracteres especiales
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "La contraseña debe contener: 1 número, 1 mayúscula, 1 minúscula, 1 carácter especial (@#$%^&+=!) y sin espacios"
    )
    private String contrasena;

    /**
     * Estado actual del usuario en el sistema.
     * Valores típicos: "ACTIVO", "INACTIVO", "PENDIENTE".
     * Controla si el usuario puede acceder al sistema.
     */
    private String estado;

    /**
     * Lista de identificadores de roles asignados al usuario.
     * Cada ID debe corresponder a un rol existente en el sistema.
     * Ejemplo: [1, 3] para roles "ADMIN" y "PROFESOR".
     */
    private List<Integer> roles;
    private boolean mfaHabilitado;

    private String email;
    private String mfaSecret;

    private boolean emailVerificado;

}