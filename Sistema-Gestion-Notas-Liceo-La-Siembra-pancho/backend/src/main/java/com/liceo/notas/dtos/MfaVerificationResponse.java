package com.liceo.notas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) utilizado para representar la respuesta
 * del proceso de verificación de autenticación de dos factores (MFA).
 * Contiene información sobre el éxito de la operación, un mensaje descriptivo y un token de acceso.
 */
@Data
@AllArgsConstructor // Genera automáticamente un constructor con todos los campos
public class MfaVerificationResponse {
    /**
     * Indica si el proceso de verificación MFA fue exitoso (true) o fallido (false).
     */
    private boolean success;

    /**
     * Mensaje descriptivo del resultado de la verificación MFA.
     * Puede contener mensajes como "Verificación MFA exitosa" o "Código inválido".
     */
    private String message;

    /**
     * Token de acceso generado tras una verificación MFA exitosa.
     * Este token se utiliza para realizar llamadas autenticadas a otros endpoints seguros.
     */
    private String token;
}