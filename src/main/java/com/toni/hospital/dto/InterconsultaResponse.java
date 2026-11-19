package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoInterconsulta;
import java.time.LocalDateTime;

/** Interconsulta con referencias simples a la atención y al servicio destino. */
public record InterconsultaResponse(
        Long id, LocalDateTime fechaSolicitud, String motivo, EstadoInterconsulta estado,
        Long atencionUrgenciasId, ReferenciaResponse servicioDestino
) {
}
