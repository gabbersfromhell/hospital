package com.toni.hospital.dto;

import com.toni.hospital.modelo.NivelTriaje;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos que se reciben para registrar una valoración de triaje.
 */
public record ValoracionTriajeRequest(

        @NotNull
        @Positive
        Long enfermeroId,

        @NotNull
        NivelTriaje nivelTriaje,

        @Size(max = 500)
        String observaciones
) {
}