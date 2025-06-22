package com.liceo.notas.dtos;

import jakarta.validation.constraints.Email;
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
    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 10, message = "La cédula debe tener exactamente 10 dígitos")
    @Pattern(regexp = "^[0-9]*$", message = "La cédula solo debe contener números")
    private String idUsuario;

    @NotBlank(message = "Los nombres son obligatorios")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Los nombres solo deben contener letras")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Los apellidos solo deben contener letras")
    private String apellidos;

    @NotBlank(message = "El nickname es obligatorio")
    @Size(min = 4, max = 15, message = "El nickname debe tener entre 4 y 10 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nickname solo puede contener letras, números, puntos, guiones bajos y guiones")
    private String nickname;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "La contraseña debe contener: 1 número, 1 mayúscula, 1 minúscula, 1 carácter especial (@#$%^&+=!) y sin espacios"
    )
    private String contrasena;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(ACTIVO|INACTIVO)$", message = "El estado debe ser ACTIVO, INACTIVO o PENDIENTE")
    private String estado;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    private List<Integer> roles;
    private boolean mfaHabilitado;
    private String mfaSecret;
    private boolean emailVerificado;
}