package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoPrescripcionMedicacion;
import com.toni.hospital.modelo.ViaAdministracion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Datos devueltos al consultar una prescripción. */
public record PrescripcionMedicacionResponse(
        Long id,
        ReferenciaResponse medicamento,
        Long medicoPrescriptorId,
        BigDecimal dosisPorAdministracion,
        String unidadDosis,
        ViaAdministracion viaAdministracion,
        String pauta,
        boolean siPrecisa,
        String indicacionSiPrecisa,
        String indicacionClinica,
        LocalDateTime fechaHoraPrescripcion,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        EstadoPrescripcionMedicacion estado,
        Long atencionUrgenciasId,
        Long ingresoHospitalarioId,
        Long citaAmbulatoriaId
) {
}