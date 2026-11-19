package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoSolicitudIngreso;
import java.time.LocalDateTime;

/** Solicitud de ingreso y sus referencias administrativas. */
public record SolicitudIngresoResponse(
        Long id, Long atencionUrgenciasId, ReferenciaResponse medicoSolicitante,
        ReferenciaResponse servicioHospitalario, LocalDateTime fechaSolicitud,
        String motivo, EstadoSolicitudIngreso estado, Long ingresoHospitalarioId
) {
}
