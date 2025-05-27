package com.liceo.notas.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// ResendVerificationEmailRequest.java
@Data
public class ResendVerificationEmailRequest {
    @NotBlank(message = "El nickname es obligatorio")
    private String nickname;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;
}