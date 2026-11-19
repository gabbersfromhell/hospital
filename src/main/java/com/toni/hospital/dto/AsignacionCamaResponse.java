package com.toni.hospital.dto;

import java.time.LocalDateTime;

/** Tramo histórico de ocupación de una cama por un ingreso. */
public record AsignacionCamaResponse(
        Long id, Long ingresoHospitalarioId, CamaResumenResponse cama,
        LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, String motivo
) {
}
