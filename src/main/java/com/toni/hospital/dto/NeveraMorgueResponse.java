package com.toni.hospital.dto;

/** Nevera de morgue; ocupada puede calcularse desde registros de exitus activos. */
public record NeveraMorgueResponse(
        Long id, String codigo, String ubicacion, boolean activa, boolean ocupada,
        ReferenciaResponse hospital
) {
}
