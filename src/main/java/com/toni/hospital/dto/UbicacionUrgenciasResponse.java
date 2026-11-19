package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoUbicacionUrgencias;

/** Ubicación de urgencias con referencia breve al hospital. */
public record UbicacionUrgenciasResponse(
        Long id, String codigo, TipoUbicacionUrgencias tipo, boolean activa,
        ReferenciaResponse hospital
) {
}
