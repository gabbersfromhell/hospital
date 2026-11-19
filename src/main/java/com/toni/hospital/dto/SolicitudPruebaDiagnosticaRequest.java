package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoProfesionalSolicitante;
import com.toni.hospital.modelo.TipoPruebaDiagnostica;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos recibidos para registrar una solicitud de prueba diagnóstica. */
public record SolicitudPruebaDiagnosticaRequest(

        @NotNull
        Long servicioDestinoId,

        @NotNull
        TipoPruebaDiagnostica tipo,

        @NotBlank
        @Size(max = 150)
        String nombrePrueba,

        @NotBlank
        @Size(max = 500)
        String indicacionClinica,

        @NotNull
        TipoProfesionalSolicitante tipoProfesionalSolicitante,

        @Size(max = 150)
        String detalleOtroProfesional,

        // El servicio exige que solo uno de estos tres IDs tenga valor.
        Long atencionUrgenciasId,
        Long ingresoHospitalarioId,
        Long citaAmbulatoriaId
) {
}