package com.toni.hospital.dto;

import com.toni.hospital.modelo.ViaAdministracion;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Datos recibidos para registrar una prescripción. */
public record PrescripcionMedicacionRequest(

        @NotNull
        Long medicamentoId,

        @NotNull
        Long medicoPrescriptorId,

        @Positive
        @Digits(integer = 8, fraction = 4)
        BigDecimal dosisPorAdministracion,

        @Size(max = 40)
        String unidadDosis,

        @NotNull
        ViaAdministracion viaAdministracion,

        @NotBlank
        @Size(max = 250)
        String pauta,

        boolean siPrecisa,

        @Size(max = 500)
        String indicacionSiPrecisa,

        @Size(max = 500)
        String indicacionClinica,

        @NotNull
        LocalDateTime fechaHoraInicio,

        LocalDateTime fechaHoraFin,

        // El servicio exigirá que solo uno de estos IDs tenga valor.
        Long atencionUrgenciasId,
        Long ingresoHospitalarioId,
        Long citaAmbulatoriaId
) {
}