package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoCama;
import jakarta.validation.constraints.NotNull;

/** Cambio del estado operativo de una cama. */
public record CambiarEstadoCamaRequest(@NotNull EstadoCama estado) {
}
