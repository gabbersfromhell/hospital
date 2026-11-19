package com.toni.hospital.dto;

import com.toni.hospital.modelo.MedioLlegadaUrgencias;
import com.toni.hospital.modelo.ProcedenciaUrgencias;
import com.toni.hospital.modelo.TipoCircuitoUrgencias;
import com.toni.hospital.modelo.TipoCobertura;
import com.toni.hospital.modelo.VinculoProfesionalAtencion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos que se reciben para registrar una atención de Urgencias.
 *
 * La fecha de llegada y el código de llamamiento los establece el servidor.
 */
public record AtencionUrgenciasRequest(

        @NotNull
        @Positive
        Long pacienteId,

        @NotNull
        @Positive
        Long hospitalAtencionId,

        @NotBlank
        @Size(max = 500)
        String motivoConsulta,

        @NotNull
        TipoCircuitoUrgencias circuitoUrgencias,

        @NotNull
        MedioLlegadaUrgencias medioLlegada,

        // Puede ser null si todavía no se conoce la procedencia.
        ProcedenciaUrgencias procedencia,

        // Opcional: ubicación inicial, si ya se ha asignado.
        Long ubicacionActualId,

        TipoCobertura tipoCobertura,

        @Size(max = 150)
        String entidadCobertura,

        VinculoProfesionalAtencion vinculoProfesional,

        boolean accidenteLaboral,

        boolean bajoCustodiaPolicial
) {
}