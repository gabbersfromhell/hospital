package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoServicioHospitalario;

/** Servicio hospitalario que puede presentar la API. */
public record ServicioHospitalarioResponse(
        Long id, String nombre, TipoServicioHospitalario tipoServicio, boolean activo
) {
}
