package com.toni.hospital.dto;

/** Identificación mínima del paciente para respuestas relacionadas. */
public record PacienteResumenResponse(Long id, String nombre, String apellidos) {
}
