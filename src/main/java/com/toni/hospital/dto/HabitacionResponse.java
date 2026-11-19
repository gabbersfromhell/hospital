package com.toni.hospital.dto;

/** Habitación con referencias breves a planta y unidad. */
public record HabitacionResponse(
        Long id, String numeroHabitacion, ReferenciaResponse planta,
        ReferenciaResponse unidadHospitalaria
) {
}
