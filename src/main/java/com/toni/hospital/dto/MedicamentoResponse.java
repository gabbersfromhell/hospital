package com.toni.hospital.dto;

import com.toni.hospital.modelo.FormaFarmaceutica;

/** Datos del medicamento que devuelve la API. */
public record MedicamentoResponse(
        Long id,
        String nombreGenerico,
        String nombreComercial,
        String principiosActivos,
        String concentracion,
        FormaFarmaceutica formaFarmaceutica,
        String presentacion,
        boolean activo
) {
}