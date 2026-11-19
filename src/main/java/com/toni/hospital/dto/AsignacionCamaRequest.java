package com.toni.hospital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Asignación de una cama a un ingreso identificado en la ruta. */
public record AsignacionCamaRequest(
        @NotNull @Positive Long camaId,
        @Size(max = 250) String motivo
) {
}
