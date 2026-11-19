package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import java.util.List;

/** Datos para crear o actualizar un hospital. */
public record HospitalRequest(
        @NotBlank @Size(max = 150) String nombre,
        @NotBlank @Size(max = 100) String municipio,
        @Size(max = 250) String direccion,
        @Size(max = 30) String telefono,
        List<@Positive Long> serviciosIds
) {
}
