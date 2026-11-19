package com.toni.hospital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/** Registro administrativo de exitus; el servicio valida que se indique un único contexto. */
public record RegistroExitusRequest(
        @NotNull @Positive Long pacienteId,
        Long atencionUrgenciasId,
        Long ingresoHospitalarioId,
        @NotNull LocalDateTime fechaHoraFallecimiento,
        @Positive Long medicoCertificadorId,
        LocalDateTime fechaHoraCertificadoMedico,
        Long neveraMorgueId,
        LocalDateTime fechaHoraEntradaMorgue,
        @Size(max = 150) String nombreFuneraria,
        LocalDateTime fechaHoraAvisoFuneraria,
        LocalDateTime fechaHoraRetirada
) {
}
