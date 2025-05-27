package com.liceo.notas.dtos;

import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) utilizado para recibir las credenciales
 * de un usuario durante el proceso de inicio de sesión.
 * Contiene el nombre de usuario o correo y la contraseña proporcionados por el usuario.
 */
@Data
public class LoginRequest {
    /**
     * Identificador único del usuario, generalmente un nickname o correo electrónico.
     * Este campo es obligatorio al iniciar sesión.
     */
    private String idUsuario;

    /**
     * Contraseña asociada a la cuenta del usuario.
     * Este campo es obligatorio y se utiliza para autenticar al usuario.
     */
    private String contrasena;
}