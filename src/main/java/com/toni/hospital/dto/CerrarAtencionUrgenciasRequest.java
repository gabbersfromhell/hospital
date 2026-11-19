package com.toni.hospital.dto;

import com.toni.hospital.modelo.ResultadoAtencionUrgencias;
import jakarta.validation.constraints.NotNull;

/** Datos para cerrar una atención; la fecha de cierre la asigna el servidor. */
public record CerrarAtencionUrgenciasRequest(
        @NotNull ResultadoAtencionUrgencias resultado
) {
}
