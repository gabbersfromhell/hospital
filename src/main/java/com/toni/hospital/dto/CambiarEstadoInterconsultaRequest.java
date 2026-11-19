package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoInterconsulta;
import jakarta.validation.constraints.NotNull;

/** Cambio controlado del estado de una interconsulta. */
public record CambiarEstadoInterconsultaRequest(
        @NotNull EstadoInterconsulta estado
) {
}
