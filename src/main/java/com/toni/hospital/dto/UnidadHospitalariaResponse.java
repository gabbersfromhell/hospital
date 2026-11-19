package com.toni.hospital.dto;

/** Unidad hospitalaria con referencias breves a sus entidades relacionadas. */
public record UnidadHospitalariaResponse(
        Long id, String codigo, String nombre, ReferenciaResponse hospital,
        ReferenciaResponse servicioHospitalario, boolean activa
) {
}
