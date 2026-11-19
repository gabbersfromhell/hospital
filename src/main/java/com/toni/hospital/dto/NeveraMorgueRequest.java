package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos para crear o actualizar una nevera de morgue. */
public record NeveraMorgueRequest(
        @NotBlank @Size(max = 30) String codigo,
        @Size(max = 120) String ubicacion,
        @NotNull @Positive Long hospitalId,
        boolean activa
) {
}