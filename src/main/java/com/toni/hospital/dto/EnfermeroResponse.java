package com.toni.hospital.dto;

/** Datos del enfermero que se devuelven al cliente. */
public record EnfermeroResponse(
        Long id,
        String nombre,
        String apellidos,
        String numeroColegiado,
        ReferenciaResponse hospital,
        boolean activo
) {
}