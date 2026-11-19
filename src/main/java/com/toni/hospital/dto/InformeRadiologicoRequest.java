package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** Datos recibidos para registrar un informe radiológico. */
public record InformeRadiologicoRequest(

        @NotNull
        Long solicitudPruebaId,

        @NotNull
        Long medicoInformanteId,

        @NotNull
        LocalDateTime fechaHoraEstudio,

        @NotBlank
        @Size(max = 10000)
        String hallazgos,

        @NotBlank
        @Size(max = 10000)
        String conclusion
) {
}