package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoCitaAmbulatoria;
import com.toni.hospital.modelo.TipoCitaAmbulatoria;

import java.time.LocalDateTime;

/**
 * Datos que devuelve la API al consultar una cita ambulatoria.
 */
public record CitaAmbulatoriaResponse(
        Long id,
        PacienteResumenResponse paciente,
        ReferenciaResponse hospital,
        ReferenciaResponse servicio,
        TipoCitaAmbulatoria tipo,
        LocalDateTime fechaHoraProgramada,
        LocalDateTime fechaHoraInicioReal,
        LocalDateTime fechaHoraFinReal,
        EstadoCitaAmbulatoria estado,
        String motivo,
        String observaciones
) {
}