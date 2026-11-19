package com.toni.hospital.dto;

import com.toni.hospital.modelo.NivelTriaje;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Nivel y observaciones de una valoración; fecha y atención vienen del servidor/ruta. */
public record ValoracionTriajeRequest(
        @NotNull NivelTriaje nivelTriaje,
        @Size(max = 500) String observaciones
) {
}
