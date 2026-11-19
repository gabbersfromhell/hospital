package com.toni.hospital.dto;

import java.util.List;

/**
 * Datos de hospital que puede devolver la API.
 */
public record HospitalResponse(
        Long id,
        String nombre,
        String municipio,
        String direccion,
        String telefono,
        List<ReferenciaResponse> servicios
) {
}