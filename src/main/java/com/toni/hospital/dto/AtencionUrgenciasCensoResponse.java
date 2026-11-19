package com.toni.hospital.dto;

import java.time.LocalDateTime;

/**
 * Información mínima para el censo administrativo de Urgencias.
 *
 * Si pacienteOculto es true, paciente y ubicacionActual serán null.
 */
public record AtencionUrgenciasCensoResponse(
        Long atencionId,
        LocalDateTime fechaHoraLlegada,
        boolean pacienteOculto,
        PacienteResumenResponse paciente,
        ReferenciaResponse ubicacionActual
) {
}