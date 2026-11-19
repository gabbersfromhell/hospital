package com.toni.hospital.dto;

/** Respuesta con referencias breves al hospital y servicio del médico. */
public record MedicoResponse(
        Long id, String nombre, String apellidos, String numeroColegiado,
        ReferenciaResponse hospital, ReferenciaResponse servicioHospitalario, boolean activo
) {
}
