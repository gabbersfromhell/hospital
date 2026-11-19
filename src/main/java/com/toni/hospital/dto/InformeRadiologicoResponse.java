package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoInformeRadiologico;

import java.time.LocalDateTime;

/** Datos que devuelve la API al consultar un informe radiológico. */
public record InformeRadiologicoResponse(
        Long id,
        Long solicitudPruebaId,
        Long medicoInformanteId,
        LocalDateTime fechaHoraEstudio,
        LocalDateTime fechaHoraInforme,
        String hallazgos,
        String conclusion,
        EstadoInformeRadiologico estado
) {
}