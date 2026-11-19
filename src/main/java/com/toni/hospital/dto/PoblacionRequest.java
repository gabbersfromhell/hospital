package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Datos para crear o actualizar una población. */
public record PoblacionRequest(
        @NotBlank @Size(max = 100) String nombre,
        Long hospitalReferenciaId
) {
}
