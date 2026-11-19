package com.toni.hospital.dto;

import java.time.LocalDateTime;

/** Justificante administrativo sin diagnóstico ni contenido clínico. */
public record JustificanteAsistenciaResponse(
        Long id, Long atencionUrgenciasId, LocalDateTime fechaHoraEmision,
        LocalDateTime fechaHoraLlegada, LocalDateTime fechaHoraFinUrgencias,
        PacienteResumenResponse paciente
) {
}
