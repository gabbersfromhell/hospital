package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoAltaHospitalaria;
import java.time.LocalDateTime;
import java.util.List;

/** Ingreso creado a partir de una solicitud médica; no incluye entidad JPA anidada. */
public record IngresoHospitalarioResponse(
        Long id, Long solicitudIngresoId, LocalDateTime fechaHoraIngreso,
        LocalDateTime fechaHoraAlta, TipoAltaHospitalaria tipoAlta,
        List<AsignacionCamaResponse> asignacionesCama
) {
}
