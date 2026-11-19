package com.toni.hospital.dto;

/** Datos breves de cama para mostrar asignaciones sin anidar el modelo completo. */
public record CamaResumenResponse(
        Long id,
        Integer numeroCama,
        String numeroHabitacion,
        Integer numeroPlanta
) {
}
