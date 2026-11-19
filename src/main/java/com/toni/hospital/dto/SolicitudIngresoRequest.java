package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Solicitud médica de ingreso; la atención se identifica mediante la ruta. */
public record SolicitudIngresoRequest(
        @NotNull @Positive Long medicoSolicitanteId,
        @NotNull @Positive Long servicioHospitalarioId,
        @NotBlank @Size(max = 500) String motivo
) {
}
