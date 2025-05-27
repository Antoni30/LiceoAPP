package com.liceo.notas.services;


public class ValidacionCedulaEcuatoriana {
    private static final int[] COEFICIENTES = {2, 1, 2, 1, 2, 1, 2, 1, 2};
    private static final int NUM_PROVINCIAS = 24;
    private static final int TERCER_DIGITO = 6;

    public static boolean validar(String cedula) {
        // Validaciones básicas
        if (cedula == null || cedula.length() != 10 || !cedula.matches("\\d+")) {
            return false;
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        int tercerDig = Integer.parseInt(cedula.substring(2, 3));

        // Validación de provincia y tercer dígito
        if (provincia < 1 || provincia > NUM_PROVINCIAS || tercerDig > TERCER_DIGITO) {
            return false;
        }

        // Algoritmo de validación (Módulo 10)
        int verificador = Integer.parseInt(cedula.substring(9));
        int suma = 0;

        for (int i = 0; i < COEFICIENTES.length; i++) {
            int valor = Integer.parseInt(cedula.substring(i, i + 1)) * COEFICIENTES[i];
            suma += (valor > 9) ? valor - 9 : valor;
        }

        int resultado = (suma % 10 == 0) ? 0 : 10 - (suma % 10);

        // Casos especiales para cédulas de extranjeros (30-XX-XXXXXX)
        if (provincia == 30 && resultado != verificador) {
            return cedula.equals("3000000000"); // Cédula genérica para extranjeros
        }

        return resultado == verificador;
    }
}