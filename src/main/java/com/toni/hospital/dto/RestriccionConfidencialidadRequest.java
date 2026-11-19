package com.toni.hospital.dto;

import com.toni.hospital.modelo.MotivoRestriccionConfidencialidad;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** Datos para activar una restricción de confidencialidad. */
public record RestriccionConfidencialidadRequest(

        @NotNull
        @Positive
        Long pacienteId,

        @NotNull
        @Positive
        Long hospitalId,

        @NotNull
        MotivoRestriccionConfidencialidad motivo
) {
}