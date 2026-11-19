package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoResultadoLaboratorio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Datos que devuelve la API al consultar un resultado. */
public record ResultadoLaboratorioResponse(
        Long id,
        Long solicitudPruebaId,
        String nombreDeterminacion,
        BigDecimal valorNumerico,
        String valorTextual,
        String unidadMedida,
        String intervaloReferencia,
        LocalDateTime fechaHoraResultado,
        EstadoResultadoLaboratorio estado
) {
}