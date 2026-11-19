package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoSalaQuirurgica;

/** Datos devueltos para una sala quirúrgica. */
public record SalaQuirurgicaResponse(
        Long id,
        Long hospitalId,
        String codigo,
        String nombre,
        TipoSalaQuirurgica tipo,
        String ubicacion,
        boolean activa
) {
}
