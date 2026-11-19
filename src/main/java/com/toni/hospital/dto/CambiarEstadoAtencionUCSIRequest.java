package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoAtencionUCSI;
import jakarta.validation.constraints.NotNull;

/** Cambio de etapa del episodio UCSI. */
public record CambiarEstadoAtencionUCSIRequest(
        @NotNull EstadoAtencionUCSI nuevoEstado,
        Long ingresoHospitalarioId
) {
}
