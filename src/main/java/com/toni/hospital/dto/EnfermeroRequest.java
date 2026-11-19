package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos necesarios para registrar o actualizar un enfermero. */
public record EnfermeroRequest(

        @NotBlank
        @Size(max = 100)
        String nombre,

        @NotBlank
        @Size(max = 150)
        String apellidos,

        @NotBlank
        @Size(max = 40)
        String numeroColegiado,

        @NotNull
        @Positive
        Long hospitalId
) {
}