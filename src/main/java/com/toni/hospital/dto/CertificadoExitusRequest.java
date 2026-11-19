// CertificadoExitusRequest.java
package com.toni.hospital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

/** Datos para registrar el certificado médico de defunción. */
public record CertificadoExitusRequest(
        @NotNull @Positive Long medicoCertificadorId,
        @NotNull LocalDateTime fechaHoraCertificadoMedico
) {
}