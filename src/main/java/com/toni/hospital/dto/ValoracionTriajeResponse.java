package com.toni.hospital.dto;

import com.toni.hospital.modelo.NivelTriaje;
import java.time.LocalDateTime;

/** Valoración de triaje registrada en una atención. */
public record ValoracionTriajeResponse(
        Long id, LocalDateTime fechaHora, NivelTriaje nivelTriaje,
        String observaciones, Long atencionUrgenciasId
) {
}
