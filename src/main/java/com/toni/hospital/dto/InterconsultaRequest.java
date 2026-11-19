package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Solicitud de valoración a otro servicio; atención y fecha se resuelven en ruta/servidor. */
public record InterconsultaRequest(
        @NotNull @Positive Long servicioDestinoId,
        @NotBlank @Size(max = 500) String motivo
) {
}
