package com.toni.hospital.dto;

/** Planta y hospital al que pertenece. */
public record PlantaResponse(Long id, Integer numeroPlanta, ReferenciaResponse hospital) {
}
