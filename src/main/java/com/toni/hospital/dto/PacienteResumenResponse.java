package com.toni.hospital.dto;

/** Datos básicos del paciente que se muestran en una cita. */
public record PacienteResumenResponse(
        Long id,
        String nombre,
        String apellidos
) {
}