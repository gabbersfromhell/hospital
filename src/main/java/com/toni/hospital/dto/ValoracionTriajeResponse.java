package com.toni.hospital.dto;

import com.toni.hospital.modelo.NivelTriaje;

import java.time.LocalDateTime;

/** Datos de una valoración de triaje que se devuelven al cliente. */
public record ValoracionTriajeResponse(
        Long id,
        LocalDateTime fechaHora,
        NivelTriaje nivelTriaje,
        String observaciones,
        Long atencionUrgenciasId,
        ReferenciaResponse enfermero
) {
}