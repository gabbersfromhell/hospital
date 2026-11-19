package com.toni.hospital.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/** Datos para programar una administración. */
public record ProgramarAdministracionMedicacionRequest(

        @NotNull
        Long prescripcionMedicacionId,

        @NotNull
        LocalDateTime fechaHoraProgramada
) {
}