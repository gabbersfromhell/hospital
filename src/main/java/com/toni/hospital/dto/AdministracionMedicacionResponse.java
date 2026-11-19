package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoAdministracionMedicacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Datos devueltos al consultar una administración de medicación. */
public record AdministracionMedicacionResponse(
        Long id,
        Long prescripcionMedicacionId,
        ReferenciaResponse medicamento,
        LocalDateTime fechaHoraProgramada,
        LocalDateTime fechaHoraAdministracion,
        LocalDateTime fechaHoraRegistroResultado,
        EstadoAdministracionMedicacion estado,
        BigDecimal dosisAdministrada,
        String unidadDosis,
        Long enfermeroResponsableId,
        String motivoNoAdministracion,
        String observaciones
) {
}