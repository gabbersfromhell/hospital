package com.toni.hospital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos para crear un episodio previsto de UCSI. */
public record AtencionUCSIRequest(
        @NotNull Long intervencionQuirurgicaId,
        @Size(max = 500) String observaciones
) {
}
