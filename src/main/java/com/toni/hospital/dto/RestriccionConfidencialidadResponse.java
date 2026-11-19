package com.toni.hospital.dto;

import java.time.LocalDateTime;

/** Estado de la restricción, sin exponer su motivo. */
public record RestriccionConfidencialidadResponse(
        Long id,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        boolean activa
) {
}