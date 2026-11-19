package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoUbicacionUrgencias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos para configurar un box, pasillo u otra ubicación de urgencias. */
public record UbicacionUrgenciasRequest(
        @NotBlank @Size(max = 30) String codigo,
        @NotNull @Positive Long hospitalId,
        @NotNull TipoUbicacionUrgencias tipo,
        boolean activa
) {
}
