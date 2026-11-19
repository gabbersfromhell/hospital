package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoCama;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** Datos para configurar una cama; su ocupación se calcula con las asignaciones. */
public record CamaRequest(
        @NotNull @Positive Integer numeroCama,
        @NotNull @Positive Long habitacionId,
        @NotNull EstadoCama estado
) {
}
