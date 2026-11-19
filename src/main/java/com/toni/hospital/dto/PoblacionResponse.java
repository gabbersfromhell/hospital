package com.toni.hospital.dto;

/** Población y referencia breve de su hospital territorial. */
public record PoblacionResponse(
        Long id,
        String nombre,
        ReferenciaResponse hospitalReferencia
) {
}
