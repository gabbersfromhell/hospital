package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos para configurar una habitación. */
public record HabitacionRequest(
        @NotBlank @Size(max = 20) String numeroHabitacion,
        @NotNull @Positive Long plantaId,
        @NotNull @Positive Long unidadHospitalariaId
) {
}