package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos para configurar una unidad hospitalaria. */
public record UnidadHospitalariaRequest(
        @NotBlank @Size(max = 30) String codigo,
        @NotBlank @Size(max = 120) String nombre,
        @NotNull @Positive Long hospitalId,
        Long servicioHospitalarioId,
        boolean activa
) {
}
