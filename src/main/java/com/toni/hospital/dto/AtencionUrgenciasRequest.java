package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoCircuitoUrgencias;
import com.toni.hospital.modelo.TipoCobertura;
import com.toni.hospital.modelo.VinculoProfesionalAtencion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos iniciales de una atención. Las fechas de llegada las asigna el servidor. */
public record AtencionUrgenciasRequest(
        @NotNull @Positive Long pacienteId,
        @NotNull @Positive Long hospitalAtencionId,
        @NotBlank @Size(max = 500) String motivoConsulta,
        @NotNull TipoCircuitoUrgencias circuitoUrgencias,
        Long ubicacionActualId,
        TipoCobertura tipoCobertura,
        @Size(max = 150) String entidadCobertura,
        VinculoProfesionalAtencion vinculoProfesional,
        boolean accidenteLaboral,
        boolean bajoCustodiaPolicial
) {
}
