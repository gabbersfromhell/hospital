package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoServicioHospitalario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos para mantener el catálogo de servicios hospitalarios. */
public record ServicioHospitalarioRequest(
        @NotBlank @Size(max = 120) String nombre,
        @NotNull TipoServicioHospitalario tipoServicio,
        boolean activo
) {
}
