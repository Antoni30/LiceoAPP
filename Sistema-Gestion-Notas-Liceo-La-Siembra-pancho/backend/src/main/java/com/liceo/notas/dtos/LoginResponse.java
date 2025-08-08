package com.liceo.notas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) utilizado para representar la respuesta
 * del proceso de inicio de sesión en el sistema.
 * Contiene información sobre el éxito de la operación, un mensaje descriptivo y un token de autenticación.
 */
@Data
@AllArgsConstructor // Genera automáticamente un constructor con todos los campos
public class LoginResponse {
    /**
     * Indica si el proceso de inicio de sesión fue exitoso (true) o fallido (false).
     */
    private boolean success;

    /**
     * Mensaje descriptivo del resultado del inicio de sesión.
     * Puede contener mensajes como "Inicio de sesión exitoso" o "Credenciales inválidas".
     */
    private String message;

    /**
     * Token de autenticación generado tras un inicio de sesión exitoso.
     * Este token se utiliza para realizar llamadas autenticadas a otros endpoints seguros.
     */
    private String token;

    public boolean isExito() {
        return  true;
    }
}