package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoCama;

/** Cama; ocupada se calcula a partir de asignaciones activas, no del estado operativo. */
public record CamaResponse(
        Long id, Integer numeroCama, EstadoCama estado, boolean ocupada,
        ReferenciaResponse habitacion
) {
}
