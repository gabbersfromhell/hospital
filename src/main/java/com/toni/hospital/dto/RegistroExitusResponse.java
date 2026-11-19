package com.toni.hospital.dto;

import java.time.LocalDateTime;

/** Registro de exitus y trazabilidad de certificado, morgue y funeraria. */
public record RegistroExitusResponse(
        Long id, PacienteResumenResponse paciente, Long atencionUrgenciasId,
        Long ingresoHospitalarioId, LocalDateTime fechaHoraFallecimiento,
        ReferenciaResponse medicoCertificador, LocalDateTime fechaHoraCertificadoMedico,
        ReferenciaResponse neveraMorgue, LocalDateTime fechaHoraEntradaMorgue,
        String nombreFuneraria, LocalDateTime fechaHoraAvisoFuneraria,
        LocalDateTime fechaHoraRetirada
) {
}
