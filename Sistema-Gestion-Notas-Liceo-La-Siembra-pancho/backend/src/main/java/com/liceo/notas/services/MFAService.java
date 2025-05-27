package com.liceo.notas.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
/**
 * Servicio para generar y validar códigos de autenticación de dos factores (MFA).
 * Los códigos generados son numéricos y tienen una duración limitada.
 */
@Service
public class MFAService {

    /**
     * Longitud fija de los códigos MFA generados.
     * Por convención estándar, se utilizan códigos de 6 dígitos.
     */
    private static final int CODE_LENGTH = 6;

    /**
     * Caracteres permitidos para la generación del código MFA.
     * En este caso solo números del 0 al 9.
     */
    private static final String NUMBERS = "0123456789";

    /**
     * Genera un código MFA numérico aleatorio de longitud fija.
     *
     * @return Cadena con el código MFA generado (solo números)
     */
    public String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(NUMBERS.length());
            sb.append(NUMBERS.charAt(randomIndex));
        }

        return sb.toString();
    }

    /**
     * Verifica si un código MFA es válido y no ha expirado.
     *
     * @param storedCode     Código almacenado durante el proceso de inicio de sesión
     * @param inputCode       Código introducido por el usuario para verificar
     * @param expirationTime  Fecha y hora de expiración del código
     * @return true si el código coincide y no ha expirado, false en caso contrario
     */
    public boolean isCodeValid(String storedCode, String inputCode, LocalDateTime expirationTime) {
        if (storedCode == null || inputCode == null || expirationTime == null) {
            return false;
        }

        return storedCode.equals(inputCode) && LocalDateTime.now().isBefore(expirationTime);
    }
}