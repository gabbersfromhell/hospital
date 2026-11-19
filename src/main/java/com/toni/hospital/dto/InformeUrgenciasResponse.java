package com.toni.hospital.dto;

import java.time.LocalDateTime;

/** Informe clínico; la autorización para consultarlo se controla fuera del DTO. */
public record InformeUrgenciasResponse(
        Long id, Long atencionUrgenciasId, ReferenciaResponse medicoResponsable,
        LocalDateTime fechaHoraEmision, String resumenClinico, String diagnostico,
        String tratamiento, String recomendaciones
) {
}
