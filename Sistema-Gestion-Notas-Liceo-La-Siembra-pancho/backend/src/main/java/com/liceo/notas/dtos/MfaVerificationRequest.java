package com.liceo.notas.dtos;

import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) utilizado para recibir los datos necesarios
 * durante el proceso de verificación de autenticación de dos factores (MFA).
 * Contiene el nombre de usuario o identificador y el código MFA proporcionado por el usuario.
 */
@Data
public class MfaVerificationRequest {
    /**
     * Identificador del usuario, generalmente su nickname o correo electrónico,
     * utilizado para asociar el código MFA a la cuenta correspondiente.
     */
    private String idUsuario;

    /**
     * Código de autenticación de dos factores (MFA) introducido por el usuario.
     * Este código suele ser generado por una aplicación autenticadora como Google Authenticator.
     */
    private String code;
}