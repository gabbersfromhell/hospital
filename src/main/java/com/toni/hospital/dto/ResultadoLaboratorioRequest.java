package com.toni.hospital.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Datos recibidos para registrar un resultado de laboratorio.
 */
public record ResultadoLaboratorioRequest(

        @NotNull
        Long solicitudPruebaId,

        @NotBlank
        @Size(max = 120)
        String nombreDeterminacion,

        @Digits(integer = 10, fraction = 4)
        BigDecimal valorNumerico,

        @Size(max = 2000)
        String valorTextual,

        @Size(max = 40)
        String unidadMedida,

        @Size(max = 120)
        String intervaloReferencia
) {
}