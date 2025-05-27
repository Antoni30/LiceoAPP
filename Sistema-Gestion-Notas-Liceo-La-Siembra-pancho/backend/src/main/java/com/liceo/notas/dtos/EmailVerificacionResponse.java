package com.liceo.notas.dtos;

import lombok.Data;

@Data
public class EmailVerificacionResponse {
    private boolean exito;
    private String mensaje;
}
